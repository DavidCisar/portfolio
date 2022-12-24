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
  public startTime = 0;
  public currentTime = 0;
  public elapsedTime = 0;
  public deltaTime = 16;

  // Scene
  public scene: THREE.Scene;

  // Light
  public sunlight: THREE.DirectionalLight;

  // Camera
  public camera: any;
  public perspectiveCamera: THREE.PerspectiveCamera;

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
  //public mixer: THREE.AnimationMixer;

  // Interaction
  public interactionManager: InteractionManager;
  public portfolio: any;
  public cv: any;

  // Booleans for Interaction
  public hideMenu: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);
  public enteredPortfolio: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public onExplore: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public loaded = false;

  // Hamburger Menu
  public menu_btn: any;

  // Resources
  public resources: Resources;
  public room: any;
  public actualRoom: any;
  public trees: any;
  public actualTrees: any;
  public backgroundPlane: THREE.Mesh;


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

    // Renderer
    this.setRenderer();

    // OrbitControl
    //this.orbitControl = new OrbitControls( this.orthographicCamera, this.renderer.domElement )

    // Scene
    this.resources.on('ready', () => {
      this.room = this.resources.getRoom();
      this.actualRoom = this.room.scene;
      this.actualRoom.position.y = -1.5;

      this.trees = this.resources.getTrees();
      this.actualTrees = this.trees.scene;
      this.actualTrees.position.y = -1.5;

      this.camera = this.perspectiveCamera;

      this.interactionManager = new InteractionManager(
        this.renderer,
        this.perspectiveCamera,
        this.canvas,
        true
      );

      this.interactionManager.add(this.actualRoom);
      this.actualRoom.addEventListener('click', (event: any) => {
        if (this.loaded) {
          this.exploreRoom();
        }
      });

      this.actualRoom.children.forEach((child: any) => {
        if (child.name === 'Portfolio') {
          this.portfolio = child;
          let portfolioColor: any;
          this.interactionManager.add(child);
          child.addEventListener('click', (event: any) => {
            if (this.onExplore) {
              this.router.navigate(['portfolio']);
            }
            });
          child.addEventListener('mouseover', (event: any) => {
            portfolioColor = event.target.material.color.getHex();
            event.target.material.color.set(0xC89D7C);
            document.body.style.cursor = 'pointer';
            });
          child.addEventListener('mouseout', (event: any) => {
            event.target.material.color.setHex(portfolioColor);
            document.body.style.cursor = 'grab';
            });
        }

        if (child.name === 'CV') {
          this.cv = child;
          let cvColor: any;
          this.interactionManager.add(child);
          child.addEventListener('click', (event: any) => {
            if (this.onExplore) {
              this.router.navigate(['about']);
            }
            });
          child.addEventListener('mouseover', (event: any) => {
            cvColor = event.target.material.color.getHex();
            event.target.material.color.set(0xC89D7C);
            document.body.style.cursor = 'pointer';
            });
          child.addEventListener('mouseout', (event: any) => {
            event.target.material.color.setHex(cvColor);
            document.body.style.cursor = 'grab';
            });
        }
      });

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
    this.perspectiveCamera = new THREE.PerspectiveCamera(37.5, this.aspect, 0.1, 1000);
    this.perspectiveCamera.position.set(0, 10, 20);
    this.perspectiveCamera.rotateX(-0.45)
    this.scene.add(this.perspectiveCamera);
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
    });
    this.actualTrees.children.forEach((child: any) => {
      child.castShadow = true;
      child.receiveShadow = true;
      if (child instanceof THREE.Group) {
        child.children.forEach((groupChild: any) => {
          groupChild.castShadow = true;
          groupChild.receiveShadow = true;
        })
      }
    })
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

  setPath(x: number, y: number, z: number, interval: number, object: any) {
    this.timeline = GSAP.timeline();
    this.timeline.to(object.position, {
      x: x,
      y: y,
      z: z,
      duration: interval
    });
  }

  setModel() {
    this.scene.add(this.actualRoom);
    this.scene.add(this.actualTrees);
    this.scene.add(this.backgroundPlane);
  }

  setLights() {
    this.sunlight = new THREE.DirectionalLight('#FFFFFF', 3);
    this.sunlight.castShadow = true;
    this.sunlight.shadow.camera.far = 100;
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

  enterPortfolio() {
    this.enteredPortfolio.next(true);
    this.startTime = Date.now();
    this.currentTime = this.startTime;
  }

  exploreRoom() {
    this.onExplore.next(true);
    this.menu_btn = null;
    this.setPath(0, 5, 7.5, 2, this.perspectiveCamera);
    //this.update();

    let menu = document.getElementById("menu")
    if (menu) {
      menu.style["animationDuration"] = '1s';
    }
  }

  goBack() {
    this.onExplore.next(false);
    //this.update();
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

    if (!this.menu_btn && !this.onExplore) {
      this.menu_btn = document.querySelector('.hamburger');
      if (this.menu_btn) {
        this.menu_btn.addEventListener('click', function() {
          document.querySelector('.hamburger')!.classList.toggle('is-active');
          document.querySelector('.navigation')!.classList.toggle('is-active');
          });
      }
    }

    if (this.actualRoom && this.enteredPortfolio.getValue()) {
      this.interactionManager.update();

      if (this.onExplore.getValue()) {
        this.perspectiveCamera.rotation.y = (-1) * this.lerp.current * 0.025;
      } else {
        this.actualRoom.rotation.y = this.lerp.current * 0.05;
      }
      if (this.elapsedTime > 2500 && !this.onExplore.getValue()) {
        this.setPath(-4.5, 10, 20, 2, this.perspectiveCamera); // this.orthographicCamera);
      }
      if (this.elapsedTime > 4000) {
        this.hideMenu.next(false);
      }
      if (this.elapsedTime > 6000) {
        this.loaded = true;
      }
    }
    if (this.camera) {
      this.renderer.render(this.scene, this.camera);
    }
    window.requestAnimationFrame(() => this.update());
  }
}
