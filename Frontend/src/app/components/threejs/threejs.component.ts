import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import * as THREE from 'three';

@Component({
  selector: 'app-threejs',
  templateUrl: './threejs.component.html',
  styleUrls: ['./threejs.component.css']
})
export class ThreejsComponent implements OnInit, AfterViewInit {

  @ViewChild('canvas')
  private canvasRef!: ElementRef;

  // Cube Properties
  @Input()
  public rotationSpeedX: number = 0.005;

  @Input()
  public rotationSpeedY: number = 0.0075;

  @Input()
  public size: number = 200;


  // Stage Properties
  @Input()
  public cameraZ: number = 5;

  @Input()
  public fieldOfView: number = 25;

  @Input('nearClipping')
  public nearClippingPlane: number = 1;

  @Input('farClipping')
  public farClippingPlane: number = 1000;


  // Helper Properties
  private camera!: THREE.PerspectiveCamera;

  private get canvas(): HTMLCanvasElement {
    return this.canvasRef.nativeElement;
  }

  private geometry = new THREE.IcosahedronGeometry(1, 0);
  private material = new THREE.MeshLambertMaterial({color: 0xda70d6});

  private icosahedron: THREE.Mesh = new THREE.Mesh(this.geometry, this.material);

  private renderer!: THREE.WebGLRenderer;

  private scene!: THREE.Scene;


  /**
   * Create the scene
   *
   * @private
   * @memberof ThreejsComponent
   */
  private createScene () {
    this.scene = new THREE.Scene();
    this.scene.background = new THREE.Color(0x000000);
    const pointLight = new THREE.PointLight(0xfffff, 10, 100);
    pointLight.position.set(25, 35, 70);

    this.scene.add(this.icosahedron, pointLight);

    let aspectRatio = this.getAspectRatio();
    this.camera = new THREE.PerspectiveCamera(
      this.fieldOfView,
      aspectRatio,
      this.nearClippingPlane,
      this.farClippingPlane
    )
    this.camera.position.z = this.cameraZ;
  }

  private getAspectRatio () {
    return this.canvas.clientWidth / this.canvas.clientHeight;
  }

  /**
   * Animate the Icosahedron
   *
   * @private
   * @memberof ThreejsComponent
   */
  private animateIcosahedron () {
    this.icosahedron.rotation.x += this.rotationSpeedX;
    this.icosahedron.rotation.y += this.rotationSpeedY;
  }

  /**
   * Start the rendering loop
   *
   * @private
   * @memberof ThreejsComponent
   */
  private startRenderingLoop() {
    this.renderer = new THREE.WebGLRenderer({ canvas: this.canvas });
    this.renderer.setPixelRatio(devicePixelRatio);
    this.renderer.setSize(this.canvas.clientWidth, this.canvas.clientHeight);

    let component: ThreejsComponent = this;
    (function render() {
      requestAnimationFrame(render);
      component.animateIcosahedron();
      component.renderer.render(component.scene, component.camera);
    }());
  }

  constructor() { }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.createScene();
    this.startRenderingLoop();
  }

}
