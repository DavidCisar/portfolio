import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import * as THREE from 'three';
import { InteractionManager } from 'three.interactive';
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
  public loaded: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public addedEventlistenerToRoom: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  // Resources
  public resources: Resources;
  public room: any;
  public actualRoom: any;
  public trees: any;
  public actualTrees: any;
  public backgroundPlane: THREE.Mesh;
  public actualParticles: any;
  public particles: THREE.Mesh[] = [];

  // Intro
  public intro = 'Welcome to my Portfolio';
  public introCharacters: string[] = [];
  public introCounter = 0;
  public introDone = false;

  // Time
  public timeHour = new Date().getHours();
  public timeMinute = new Date().getMinutes();
  public timeSecond = new Date().getSeconds();
  public timeOfDay = 0;

  constructor(private router: Router) {
    this.resources = new Resources();
    GSAP.registerPlugin(ScrollTrigger);

    let introInterval = setInterval(() => {
      for (let i = 0; i < this.introCounter; i++) {
        this.introCharacters[i] = this.intro.charAt(i);
      }
      for (let i = this.introCounter; i < this.intro.length; i++) {
        this.introCharacters[i] = Math.random().toString(36).charAt(2);
      }
      if (document.getElementById('hacking')) {
        document.getElementById('hacking')!.innerText = this.introCharacters.join('');
      }
    }, 50);

    let introRevealInterval = setInterval(() => {
      if (!this.introDone) {
        this.introCounter++;
        if (this.introCounter == this.intro.length) {
          this.introDone = true;
        }
      }
    }, 125);

    let timeUpdate = setInterval(() => {
      this.timeHour = new Date().getHours();
      this.timeMinute = new Date().getMinutes();
      this.timeSecond = new Date().getSeconds();

      this.timeOfDay = (this.timeHour * 3600 + this.timeMinute * 60 + this.timeSecond) / 86400;
    }, 1000);
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
      this.room = this.resources.getRoom();
      this.actualRoom = this.room.scene;
      this.actualRoom.position.y = -1.5;

      this.actualParticles = this.resources.getParticles().scene;
      this.actualParticles.position.y = -1.5;

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
        if (this.loaded.getValue()) {
          this.exploreRoom();
        }
      });

      this.actualParticles.children.forEach((child: any) => {
        if (child.name.includes('Particle')) {
          child.material = new THREE.MeshNormalMaterial();
          this.particles.push(child);
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
    this.perspectiveCamera = new THREE.PerspectiveCamera(35, this.aspect, 0.1, 1000);
    this.perspectiveCamera.position.set(0, 12.5, 25);
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
    this.scene.add(this.backgroundPlane);
    this.scene.add(this.actualRoom);
    this.scene.add(this.actualParticles);
    this.scene.add(this.actualTrees);
  }

  setLights() {
    this.sunlight = new THREE.DirectionalLight('#FFFFFF', 3);
    this.sunlight.castShadow = true;
    this.sunlight.shadow.mapSize.set(2048, 2048);
    this.sunlight.shadow.normalBias = 0.05;
    this.sunlight.position.set(-1, 5, 2);
    this.scene.add(this.sunlight);

    const ambientLight = new THREE.AmbientLight(0xFFFFFF, 2);
    ambientLight.position.set(0, 2, 2);
    this.scene.add(ambientLight);
  }

  enterPortfolio() {
    this.startTime = Date.now();
    this.enteredPortfolio.next(true);
  }

  exploreRoom() {
    this.onExplore.next(true);
    let exploreElement = document.getElementById('menu');
    if (exploreElement != null) {
      exploreElement.classList.add('hidden');
    }

    let timeElement = document.getElementById('time');
    if (timeElement != null) {
      timeElement.classList.add('hidden');
    }

    this.setPath(0, 5, 7.5, 2, this.perspectiveCamera);
    this.interactionManager.update();

    if (!this.addedEventlistenerToRoom.getValue()) {
    this.actualRoom.children.forEach((child: any) => {
      if (child.name === 'Portfolio') {
        this.portfolio = child;
        let portfolioColor: any;
        this.interactionManager.add(child);
        child.addEventListener('click', (event: any) => {
          if (this.onExplore.getValue()) {
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
          if (this.onExplore.getValue()) {
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

      if (child.name === 'Skills') {
        this.cv = child;
        let cvColor: any;
        this.interactionManager.add(child);
        child.addEventListener('click', (event: any) => {
          if (this.onExplore.getValue()) {
            this.router.navigate(['skills']);
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
    this.addedEventlistenerToRoom.next(true);
    }

    let menu = document.getElementById("menu")
    if (menu) {
      menu.style["animationDuration"] = '1s';
    }
  }

  goBack() {
    this.onExplore.next(false);
    let exploreElement = document.getElementById('menu');
    if (exploreElement != null) {
      exploreElement.classList.remove('hidden');
    }

    let timeElement = document.getElementById('time');
    if (timeElement != null) {
      timeElement.classList.remove('hidden');
    }
  }

  animateParticles() {
    let counter = -15;
    for (let particle of this.particles) {
      particle.rotation.x += 0.0005 * counter * 0.25;
      particle.rotation.y -= 0.00075 * counter * 0.25;
      particle.rotation.z += 0.00025 * counter * 0.25;
      counter++;
    }
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

    if (this.actualRoom && this.enteredPortfolio.getValue()) {
      this.interactionManager.update();

      this.animateParticles();

      if (this.onExplore.getValue()) {
        this.perspectiveCamera.rotation.y = (-1) * this.lerp.current * 0.025;
      } else {
        this.actualRoom.rotation.y = this.lerp.current * 0.025;
        this.actualParticles.rotation.y = this.lerp.current * 0.05;
        this.actualTrees.rotation.y = this.lerp.current * 0.035
      }
      if (this.elapsedTime > 2500 && !this.onExplore.getValue()) {
        this.setPath(0, 10, 20, 2, this.perspectiveCamera); // this.orthographicCamera);
      }
      if (this.elapsedTime > 4000) {
        this.hideMenu.next(false);
      }
      if (this.elapsedTime > 6000) {
        this.loaded.next(true);
      }
    }

    if (this.camera) {
      this.renderer.render(this.scene, this.camera);
    }
    window.requestAnimationFrame(() => this.update());
  }
}
