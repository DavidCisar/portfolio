import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import * as THREE from 'three';
import GSAP from 'gsap';
import Resources from '.././room/resources';

@Component({
  selector: 'app-head-component',
  templateUrl: './head-component.component.html',
  styleUrls: ['./head-component.component.css']
})
export class HeadComponentComponent {
  // Canvas
  @ViewChild('canvas') canvasReference : ElementRef;
  get canvas(): HTMLCanvasElement {
    return this.canvasReference.nativeElement;
  }

  // Sizes
  public width: any;
  public height: any;
  public aspect: any;
  public pixelRatio: any;

  // Scene
  public scene: THREE.Scene;

  // Light
  public sunlight: THREE.DirectionalLight;

  // Camera
  public camera: any;
  public perspectiveCamera: THREE.PerspectiveCamera;

  // Rotation
  public lerpX = { current: 0, target: 0, ease: 0.1 };
  public lerpY = { current: 0, target: 0, ease: 0.1 };
  public rotationX: number;
  public rotationY: number;

  // Timeline
  public timeline: any;

  // Renderer
  public renderer: THREE.WebGLRenderer;

  // Resources
  public resources: Resources;
  public head: any;
  public actualHead: any;

  constructor(private router: Router) {
    this.resources = new Resources();
  }

  ngAfterViewInit(): void {
    this.scene = new THREE.Scene();

    // Sizes
    this.width = window.innerWidth;
    this.height = window.innerHeight;
    this.aspect = this.width / this.height;
    this.pixelRatio = Math.min(window.devicePixelRatio, 2);

    window.addEventListener('resize', () => {
      this.width = window.innerWidth;
      this.height = window.innerHeight;
      this.aspect = this.width / this.height;
      this.pixelRatio = Math.min(window.devicePixelRatio, 2);
      this.resize();
    });

    // Camera
    this.createPerspectiveCamera();

    // Renderer
    this.setRenderer();

    // Scene
    this.resources.on('ready', () => {
      this.head = this.resources.getHead();
      this.actualHead = this.head.scene;
      this.actualHead.position.set(0, 0, 0);

      this.camera = this.perspectiveCamera;

      this.setModel();
      this.setLights();

      // Shadows
      this.setShadows();

      // Animation
      this.onMouseMove();
    });

    this.update();
  }

  createPerspectiveCamera() {
    this.perspectiveCamera = new THREE.PerspectiveCamera(35, this.aspect, 0.1, 1000);
    this.perspectiveCamera.position.set(0, 0, 10);
    this.scene.add(this.perspectiveCamera);
  }

  setShadows() {
    this.actualHead.children.forEach((child: any) => {
      child.castShadow = true;
      child.receiveShadow = true;
      child.material = new THREE.MeshNormalMaterial();
      if (child instanceof THREE.Group) {
        child.children.forEach((groupChild: any) => {
          groupChild.castShadow = true;
          groupChild.receiveShadow = true;
          groupChild.material = new THREE.MeshNormalMaterial();
        });
      }
    });
  }

  setRenderer() {
    this.renderer = new THREE.WebGLRenderer({
      canvas: this.canvas,
      antialias: true,
    });
    this.renderer.physicallyCorrectLights = true;
    this.renderer.outputEncoding = THREE.sRGBEncoding;
    this.renderer.toneMapping = THREE.CineonToneMapping;
    this.renderer.toneMappingExposure = 1.75;
    this.renderer.shadowMap.enabled = true;
    this.renderer.shadowMap.type = THREE.PCFSoftShadowMap;
    this.renderer.setSize(this.width, this.height);
    this.renderer.setPixelRatio(this.pixelRatio);
  }

  setModel() {
    this.scene.add(this.actualHead);
    this.scene.background = new THREE.Color('#060B19');
  }

  setLights() {
    this.sunlight = new THREE.DirectionalLight('#FFFFFF', 5);
    this.sunlight.shadow.mapSize.set(2048, 2048);
    this.sunlight.shadow.normalBias = 0.05;
    this.sunlight.position.set(-1, 5, 10);
    this.scene.add(this.sunlight);
  }

  onMouseMove() {
    window.addEventListener("mousemove", (e) => {
      this.rotationX = ((e.clientX - window.innerWidth / 2) * 2) / window.innerWidth;
      this.lerpX.target = this.rotationX;
      this.rotationY = ((e.clientY - window.innerHeight / 2)) / window.innerHeight;
      this.lerpY.target = this.rotationY;
    });
  }

  resize() {
    // Updating perspectiveCamera on resize
    this.perspectiveCamera.aspect = this.aspect;
    this.perspectiveCamera.updateProjectionMatrix();

    // Updating Renderer
    this.renderer.setSize(this.width, this.height);
    this.renderer.setPixelRatio(this.pixelRatio);
  }

  update() {
    this.lerpX.current = GSAP.utils.interpolate(
      this.lerpX.current,
      this.lerpX.target,
      this.lerpX.ease
    );

    this.lerpY.current = GSAP.utils.interpolate(
      this.lerpY.current,
      this.lerpY.target,
      this.lerpY.ease
    );

    if (this.actualHead) {
      this.actualHead.rotation.y = this.lerpX.current * 0.5;
      this.actualHead.rotation.x = this.lerpY.current * 0.5;
    }

    if (this.camera) {
      this.renderer.render(this.scene, this.camera);
    }
    window.requestAnimationFrame(() => this.update());
  }
}
