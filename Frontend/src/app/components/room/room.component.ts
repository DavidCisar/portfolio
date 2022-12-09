import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import * as THREE from 'three';
import { InteractionManager } from 'three.interactive';
//import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import GSAP from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';
import Resources from './Utils/Resources';

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.css']
})
export class RoomComponent {

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
  public frustrum: any;

  // Time
  public startTime = Date.now();
  public currentTime = this.startTime;
  public elapsedTime = 0;
  public deltaTime = 16;

  // Scene
  public scene: THREE.Scene;

  // Light
  public sunlight: THREE.DirectionalLight;

  // Camera
  public camera: any;
  public perspectiveCamera: THREE.PerspectiveCamera;
  public orthographicCamera: THREE.OrthographicCamera;

  // Rotation
  public lerp = { current: 0, target: 0, ease: 0.1 };
  public rotation: number;

  // Timeline
  public timeline: any;

  // OrbitControl
  //public orbitControl: OrbitControls;

  // Renderer
  public renderer: THREE.WebGLRenderer;

  // Animation
  public mixer: THREE.AnimationMixer;

  // Interaction
  public interactionManager: InteractionManager;
  public onExplore = false;
  public monitor: any;

  // Resources
  public resources: Resources;
  public room: any;
  public actualRoom: any;
  public backgroundPlane: THREE.Mesh;
  public hideMenu: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);

  constructor(private router: Router) {
    this.resources = new Resources();
    GSAP.registerPlugin(ScrollTrigger);
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
    this.createOrthographicCamera();

    // Renderer
    this.setRenderer();

    // OrbitControl
    //this.orbitControl = new OrbitControls( this.orthographicCamera, this.renderer.domElement )

    // Scene
    this.resources.on('ready', () => {
      this.room = this.resources.getRoom();
      this.actualRoom = this.room.scene;
      this.actualRoom.position.y = -1.5;

      this.camera = this.orthographicCamera;

      this.interactionManager = new InteractionManager(
        this.renderer,
        this.orthographicCamera,
        this.canvas,
        true
      );

      this.createBackground();
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
    this.perspectiveCamera.position.set(0, 4, 7.5);
    this.perspectiveCamera.rotateX(-0.3785)
    this.scene.add(this.perspectiveCamera);
  }

  createOrthographicCamera() {
    this.frustrum = 15;
    this.orthographicCamera = new THREE.OrthographicCamera(
      (-this.aspect * this.frustrum ) / 2,
      (this.aspect * this.frustrum) / 2,
      this.frustrum / 2,
      -this.frustrum / 2,
      -50,
      50
    );

    // Adjust Camera Position
    this.orthographicCamera.position.x = 0;
    this.orthographicCamera.position.y = 2.5;
    this.orthographicCamera.position.z = 4;
    this.orthographicCamera.lookAt(new THREE.Vector3(0, 0, 0));

    this.scene.add(this.orthographicCamera);
  }

  createBackground() {
    const planeGeometry = new THREE.PlaneGeometry(200, 200);
    const planeMaterial = new THREE.MeshStandardMaterial({ color: 0x90EE90 });
    this.backgroundPlane = new THREE.Mesh(planeGeometry, planeMaterial);
    this.backgroundPlane.position.y = -2;
    this.backgroundPlane.rotateX(-0.785 * 2);
  }

  setShadows() {
    this.backgroundPlane.receiveShadow = true;
    this.actualRoom.children.forEach((child: any) => {
      child.castShadow = true;
      child.receiveShadow = true;
      if (child instanceof THREE.Group) {
        child.children.forEach((groupChild: any) => {
          groupChild.castShadow = true;
          groupChild.receiveShadow = true;
        });
      }

      if (child.name === 'Picture') {
        const paintingTexture = new THREE.TextureLoader().load('./assets/three/textures/matrix.jpg');
        child.material = new THREE.MeshBasicMaterial({
          map: paintingTexture
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

  setPath(x: number, interval: number, object: any) {
    this.timeline = GSAP.timeline();
    this.timeline.to(object.position, {
      x: x,
      duration: interval
    });
  }

  setModel() {
    this.scene.add(this.actualRoom);
    this.scene.add(this.backgroundPlane);
  }

  setLights() {
    this.sunlight = new THREE.DirectionalLight('#FFFFFF', 3);
    this.sunlight.castShadow = true;
    this.sunlight.shadow.camera.far = 50;
    this.sunlight.shadow.mapSize.set(2048, 2048);
    this.sunlight.shadow.normalBias = 0.05;
    this.sunlight.position.set(-1, 5, 3);
    this.sunlight.lookAt(new THREE.Vector3(5, 0, -50));
    this.scene.add(this.sunlight);

    const ambientLight = new THREE.AmbientLight(0xFFFFFF, 3);
    ambientLight.position.set(0, 2.5, 0);
    this.scene.add(ambientLight);

/*     const light = new THREE.PointLight( 0xffffff, 5, 100 );
        light.position.set( 0, 2, -3 );
        this.scene.add( light ); */
  }

  exploreRoom() {
    this.onExplore = true;
    this.camera = this.perspectiveCamera;
    this.interactionManager = new InteractionManager(
      this.renderer,
      this.perspectiveCamera,
      this.canvas,
      true
      );

    this.actualRoom.children.forEach((child: any) => {
      if (child.name === 'Monitor') {
        this.monitor = child;
        this.interactionManager.add(child);
        child.addEventListener('click', (event: any) => {
          this.router.navigate(['portfolio']);
          });
        child.addEventListener('mouseover', (event: any) => {
          document.body.style.cursor = 'help';
          });
        child.addEventListener('mouseout', (event: any) => {
          document.body.style.cursor = 'pointer';
          });
      }
    });

    this.update();

    let menu = document.getElementById("menu")
    if (menu) {
      menu.style["animationDuration"] = '1s';
    }

  }

  goBack() {
    this.onExplore = false;
    this.camera = this.orthographicCamera;
    this.interactionManager.remove(this.monitor);
  }

  onMouseMove() {
    window.addEventListener("mousemove", (e) => {
      this.rotation = ((e.clientX - window.innerWidth / 2) * 2) / window.innerWidth;
      this.lerp.target = this.rotation;
    });
  }

  resize() {
    // Updating perspectiveCamera on resize
    this.perspectiveCamera.aspect = this.aspect;
    this.perspectiveCamera.updateProjectionMatrix();

    // Updating orthographicCamera on resize
    this.orthographicCamera.left = (-this.aspect * this.frustrum) / 2
    this.orthographicCamera.right = (this.aspect * this.frustrum) / 2;
    this.orthographicCamera.top = this.frustrum / 2;
    this.orthographicCamera.bottom = -this.frustrum / 2;
    this.orthographicCamera.updateProjectionMatrix();

    // Updating Renderer
    this.renderer.setSize(this.width, this.height);
    this.renderer.setPixelRatio(this.pixelRatio);
  }

  update() {
    const currentTime = Date.now();
    this.deltaTime = currentTime - this.currentTime;
    this.currentTime = currentTime;
    this.elapsedTime = this.currentTime - this.startTime;
    this.lerp.current = GSAP.utils.interpolate(
      this.lerp.current,
      this.lerp.target,
      this.lerp.ease
    );
    if (this.actualRoom) {
      this.interactionManager.update();

      if (this.onExplore) {
        this.perspectiveCamera.rotation.y = (-1) * this.lerp.current * 0.05;
      } else {
        this.actualRoom.rotation.y = this.lerp.current * 0.05;
      }
      if (this.elapsedTime > 3000) {
        this.setPath(-5, 3, this.orthographicCamera);
      }
      if (this.elapsedTime > 4000) {
        this.hideMenu.next(false);
      }
    }
    if (this.camera) {
      this.renderer.render(this.scene, this.camera);
    }
    window.requestAnimationFrame(() => this.update());
  }
}
