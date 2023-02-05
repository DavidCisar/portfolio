import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import * as THREE from 'three';
import { InteractionManager } from 'three.interactive';
import GSAP from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';
import Resources from './resources';

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

  // Time
  public startTime = 0;
  public currentTime = 0;
  public elapsedTime = 0;
  public deltaTime = 16;

  // Scene
  public scene: THREE.Scene;

  // Light
  public sunlight: THREE.DirectionalLight;
  public lightStrength: number = 0.3;

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

  // Interaction
  public interactionManager: InteractionManager;
  public portfolio: any;
  public cv: any;

  // Booleans for Interaction
  public hideMenu: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);
  public enteredPortfolio: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public onExplore: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public addedEventlistenerToRoom: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public lookingAtPortfolio: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public portfolioAll: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);
  public setInitialCameraPosition: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public compactRooms: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);
  public scrolledFar: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public setOverflowVisible: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  // Resources
  public resources: Resources;
  public room_top: any;
  public room_center: any;
  public room_bottom: any;
  public actualTopRoom: any;
  public actualCenterRoom: any;
  public actualBottomRoom: any;
  public actualTopRoomPosition: any;
  public actualCenterRoomPosition: any;
  public actualBottomRoomPosition: any;

  // Intro
  public introTop = 'Hey I\'m David';
  public introCharactersTop: string[] = [];
  public introCounterTop = 0;
  public introDoneTop = false;
  public introBottom = 'Welcome to my Portfolio!';
  public introCharactersBottom: string[] = [];
  public introCounterBottom = 0;
  public introDoneBottom = false;
  public introDone: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  // Time
  public timeHour: string = '';
  public timeMinute: string = '';
  public timeSecond: string = '';

  constructor(private router: Router) {
    this.resources = new Resources();
    GSAP.registerPlugin(ScrollTrigger);

    let visibleTopLetterIndex: number[] = [];
    let hiddenTopLetterIndex: number[] = [];
    let visibleBottomLetterIndex: number[] = [];
    let hiddenBottomLetterIndex: number[] = [];

    for (let i = 0; i < this.introTop.length; i++) {
      hiddenTopLetterIndex.push(i);
    }

    for (let i = 0; i < this.introBottom.length; i++) {
      hiddenBottomLetterIndex.push(i);
    }

    let introInterval = setInterval(() => {

      // TOP HACKING
      for (let i = 0; i < this.introTop.length; i++) {
        if (!visibleTopLetterIndex.includes(i)) {
          this.introCharactersTop[i] = Math.random().toString(36).charAt(2);
        } else {
          this.introCharactersTop[i] = this.introTop.charAt(i);
        }
      }
      if (document.getElementById('hacking-1')) {
        document.getElementById('hacking-1')!.innerText = this.introCharactersTop.join('');
      }

      // BOTTOM HACKING
      for (let i = 0; i < this.introBottom.length; i++) {
        if (!visibleBottomLetterIndex.includes(i)) {
          this.introCharactersBottom[i] = Math.random().toString(36).charAt(2);
        } else {
          this.introCharactersBottom[i] = this.introBottom.charAt(i);
        }
      }
      if (document.getElementById('hacking-2')) {
        document.getElementById('hacking-2')!.innerText = this.introCharactersBottom.join('');
      }
    }, 45);

    let introTopRevealInterval = setInterval(() => {
      if (!this.introDoneTop) {
        // Math random aus den hidden
        let randomTopIndex = hiddenTopLetterIndex[Math.floor(Math.random() * (hiddenTopLetterIndex.length))];
        visibleTopLetterIndex.push(randomTopIndex);
        let removeIndex = hiddenTopLetterIndex.indexOf(randomTopIndex);
        if (removeIndex > -1) {
          hiddenTopLetterIndex.splice(removeIndex, 1);
        }
        this.introCounterTop++;
        if (this.introCounterTop == this.introTop.length) {
          this.introDoneTop = true;
        }
      }
    }, 200);

    let introBottomRevealInterval = setInterval(() => {
      if (!this.introDoneBottom) {
        let randomBottomIndex = hiddenBottomLetterIndex[Math.floor(Math.random() * (hiddenBottomLetterIndex.length))];
        visibleBottomLetterIndex.push(randomBottomIndex);
        let removeIndex = hiddenBottomLetterIndex.indexOf(randomBottomIndex);
        if (removeIndex > -1) {
          hiddenBottomLetterIndex.splice(removeIndex, 1);
        }
        this.introCounterBottom++;
        if (this.introCounterBottom == this.introBottom.length) {
          this.introDoneBottom = true;
        }
      }
      if (!this.introDone.getValue() && this.introDoneTop && this.introDoneBottom) {
        this.introDone.next(true);
      }
    }, 110);
  }

  ngAfterViewInit(): void {
    document.body.style.overflowY = "hidden";

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

    window.addEventListener('scroll', () => {
      let y = window.scrollY;
      let exploreElement = document.getElementById('menu');
      let arrow = document.getElementById('arrow');

      if (this.actualTopRoom && this.actualCenterRoom && this.actualBottomRoom && this.setInitialCameraPosition.getValue()) {
        this.actualTopRoom.rotation.x = -Math.PI / ((2*(y+800))/(y + 0.001));
        this.actualCenterRoom.rotation.x = -Math.PI / ((2*(y+800))/(y + 0.001));
        this.actualBottomRoom.rotation.x = -Math.PI / ((2*(y+800))/(y + 0.001));
      }

      if (this.actualTopRoom && this.actualCenterRoom && this.actualBottomRoom && y >50) {
        if (!this.compactRooms.getValue()) {
          this.setPath(0, 0, 0, 2, this.actualTopRoom);
          this.setPath(0, 0, 0, 2, this.actualCenterRoom);
          this.setPath(0, 0, 0, 2, this.actualBottomRoom);
          if (window.innerWidth <= 767) {
            this.setPath(0, 20, 50, 1, this.perspectiveCamera);
            GSAP.timeline().to(this.perspectiveCamera.rotation, {
              x: -0.2,
              y: 0,
              duration: 2
            });
          } else {
            this.setPath(0, 18, 42.5, 1, this.perspectiveCamera);
            GSAP.timeline().to(this.perspectiveCamera.rotation, {
              x: -0.2,
              y: 0,
              duration: 2
            })
          }
          this.onExplore.next(false);
          this.lookingAtPortfolio.next(false);
          this.compactRooms.next(true);
        }
      }

      if (exploreElement != null && arrow != null) {
        if (y > 50) {
          if (!this.scrolledFar.getValue()) {
            exploreElement.classList.add('hidden');
            arrow.classList.add('disappear');
            this.scrolledFar.next(true);
          }
        } else {
          if (this.scrolledFar.getValue()) {
            exploreElement.classList.remove('hidden');
            arrow.classList.remove('disappear');
            this.scrolledFar.next(false);
          }
        }
      }
    });

    // Camera
    this.createPerspectiveCamera();

    // Renderer
    this.setRenderer();

    // Scene
    this.resources.on('ready', () => {
      this.room_top = this.resources.getTopRoom();
      this.actualTopRoom = this.room_top.scene;
      this.actualTopRoomPosition = this.actualTopRoom.position;

      this.room_center = this.resources.getCenterRoom();
      this.actualCenterRoom = this.room_center.scene;
      this.actualCenterRoomPosition = this.actualCenterRoom.position;

      this.room_bottom = this.resources.getBottomRoom();
      this.actualBottomRoom = this.room_bottom.scene;
      this.actualBottomRoomPosition = this.actualBottomRoom.position;

      this.camera = this.perspectiveCamera;

      this.interactionManager = new InteractionManager(
        this.renderer,
        this.perspectiveCamera,
        this.canvas,
        true
      );

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
    this.perspectiveCamera.position.set(0, 50, 0);
    this.perspectiveCamera.rotateX(-Math.PI/2);
    this.scene.add(this.perspectiveCamera);
  }

  setShadows() {
    this.actualTopRoom.children.forEach((child: any) => {
      child.castShadow = true;
      child.receiveShadow = true;
      if (child instanceof THREE.Group) {
        child.children.forEach((groupChild: any) => {
          groupChild.castShadow = true;
          groupChild.receiveShadow = true;
        });
      }
    });
    this.actualCenterRoom.children.forEach((child: any) => {
      child.castShadow = true;
      child.receiveShadow = true;
      if (child instanceof THREE.Group) {
        child.children.forEach((groupChild: any) => {
          groupChild.castShadow = true;
          groupChild.receiveShadow = true;
        });
      }
    });

    this.actualBottomRoom.children.forEach((child: any) => {
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
    this.scene.add(this.actualTopRoom);
    this.scene.add(this.actualCenterRoom);
    this.scene.add(this.actualBottomRoom);
    this.scene.background = new THREE.Color('#060B19');
  }

  scrollTo(element: any): void {
    (document.getElementById(element) as HTMLElement).scrollIntoView({behavior: "smooth", block: "start", inline: "nearest"});
  }

  setLights() {
    this.sunlight = new THREE.DirectionalLight('#FFFFFF', 0.25);
    this.sunlight.shadow.mapSize.set(2048, 2048);
    this.sunlight.shadow.normalBias = 0.05;
    this.sunlight.position.set(-1, 5, 2);
    this.scene.add(this.sunlight);

    const techLight = new THREE.PointLight(0xFF6863, 3, 10); //new THREE.AmbientLight(0xFFFFFF, 2);
    techLight.position.set(-1, 2, -1);
    this.scene.add(techLight);

    const aboutLight = new THREE.PointLight(0xFFA500, 3, 10); //new THREE.AmbientLight(0xFFFFFF, 2);
    aboutLight.position.set(-1, 10, -1);
    this.scene.add(aboutLight);

    const livingRoomLight = new THREE.PointLight(0x00FFFF, 3, 100); //new THREE.AmbientLight(0xFFFFFF, 2);
    livingRoomLight.position.set(-1, 16, -2);
    this.scene.add(livingRoomLight);

    const blinkLight = new THREE.PointLight(0xFF0000, 3, 5); //new THREE.AmbientLight(0xFFFFFF, 2);
    blinkLight.position.set(0, 25, 0);
    this.scene.add(blinkLight);
  }

  enterPortfolio() {
    this.startTime = Date.now();
    this.enteredPortfolio.next(true);
  }

  delay(time: any) {
    return new Promise(resolve => setTimeout(resolve, time));
  }

  exploreMore() {
    document.body.style.overflowY = "hidden";
    this.compactRooms.next(false);
    this.onExplore.next(true);
    let exploreElement = document.getElementById('menu');
    let arrow = document.getElementById('arrow');
    if (exploreElement != null && arrow != null) {
      exploreElement.classList.add('hidden');
      arrow.classList.add('disappear');
      arrow.style["animationDuration"] = '1s';
    }

    this.setPath(0, -10, 0, 2, this.actualBottomRoom);
    this.setPath(0, -10, 0, 2, this.actualCenterRoom);
    this.setPath(0, 16.5, 20, 2, this.perspectiveCamera);
    GSAP.timeline().to(this.perspectiveCamera.rotation, {
      x: 0,
      y: 0,
      duration: 2
    });
    this.interactionManager.update();
  }

  exploreAbout() {
    document.body.style.overflowY = "hidden";
    this.compactRooms.next(false);
    this.onExplore.next(true);
    let exploreElement = document.getElementById('menu');
    let arrow = document.getElementById('arrow');
    if (exploreElement != null && arrow != null) {
      exploreElement.classList.add('hidden');
      arrow.classList.add('disappear');
      arrow.style["animationDuration"] = '1s';
    }

    this.setPath(0, 20, 0, 2, this.actualTopRoom);
    this.setPath(0, -20, 0, 2, this.actualBottomRoom);
    this.setPath(0, 10, 20, 2, this.perspectiveCamera);
    GSAP.timeline().to(this.perspectiveCamera.rotation, {
      x: 0,
      y: 0,
      duration: 2
    });
    this.interactionManager.update();
  }

  exploreTech() {
    document.body.style.overflowY = "hidden";
    this.compactRooms.next(false);
    this.onExplore.next(true);
    let exploreElement = document.getElementById('menu');
    let arrow = document.getElementById('arrow');

    if (exploreElement != null && arrow != null) {
      exploreElement.classList.add('hidden');
      arrow.classList.add('disappear');
      arrow.style["animationDuration"] = '1s';
    }

    this.setPath(0, 20, 0, 2, this.actualTopRoom);
    this.setPath(0, 20, 0, 2, this.actualCenterRoom);
    this.setPath(0, 3, 20, 2, this.perspectiveCamera);
    GSAP.timeline().to(this.perspectiveCamera.rotation, {
      x: 0,
      y: 0,
      duration: 2
      });
    this.interactionManager.update();
  }

  async goBack() {
    this.onExplore.next(false);
    this.hideMenu.next(false);

    let exploreElement = document.getElementById('menu');
    let arrow = document.getElementById('arrow');
    console.log(exploreElement);
    console.log(arrow);

    if (exploreElement != null) {
      exploreElement.classList.remove('hidden');
    }

    this.setPath(0, 0, 0, 2, this.actualTopRoom);
    this.setPath(0, 0, 0, 2, this.actualCenterRoom);
    this.setPath(0, 0, 0, 2, this.actualBottomRoom);

    if (window.innerWidth <= 767) {
      this.setPath(0, 20, 50, 1, this.perspectiveCamera);
      GSAP.timeline().to(this.perspectiveCamera.rotation, {
        x: -0.2,
        y: 0,
        duration: 2
      });
    } else {
      this.setPath(0, 18, 42.5, 1, this.perspectiveCamera);
      GSAP.timeline().to(this.perspectiveCamera.rotation, {
        x: -0.2,
        y: 0,
        duration: 2
      })
    }

    await this.delay(2500);
    document.body.style.overflowY = "visible";
    this.router.navigate(['/']);
  }

  onMouseMove() {
    window.addEventListener("mousemove", (e) => {
      this.rotationX = ((e.clientX - window.innerWidth / 2) * 2) / window.innerWidth;
      this.lerpX.target = this.rotationX;
      /* this.rotationY = ((e.clientY - window.innerHeight / 2)) / window.innerHeight;
      this.lerpY.target = this.rotationY; */
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
    this.lerpX.current = GSAP.utils.interpolate(
      this.lerpX.current,
      this.lerpX.target,
      this.lerpX.ease
    );

    /* this.lerpY.current = GSAP.utils.interpolate(
      this.lerpY.current,
      this.lerpY.target,
      this.lerpY.ease
    ); */

    if (this.actualTopRoom && this.actualCenterRoom && this.actualBottomRoom && this.enteredPortfolio.getValue()) {
      this.interactionManager.update();

      this.actualTopRoom.rotation.y = this.lerpX.current * 0.1;
      this.actualCenterRoom.rotation.y = this.lerpX.current * 0.15;
      this.actualBottomRoom.rotation.y = this.lerpX.current * 0.125;

      if (this.elapsedTime > 2500 && !this.onExplore.getValue() && !this.setInitialCameraPosition.getValue()) {
        if (window.innerWidth <= 767) {
          this.setPath(0, 20, 50, 3, this.perspectiveCamera);
          GSAP.timeline().to(this.perspectiveCamera.rotation, {
            x: -0.2,
            y: 0,
            duration: 3
          });
        } else {
          this.setPath(0, 18, 42.5, 3, this.perspectiveCamera);
          GSAP.timeline().to(this.perspectiveCamera.rotation, {
            x: -0.2,
            y: 0,
            duration: 3
          })
        }
        this.setInitialCameraPosition.next(true);
      }
      if (this.elapsedTime > 5500) {
        this.hideMenu.next(false);
      }
      if (this.elapsedTime > 6000 && this.setInitialCameraPosition.getValue() && !this.setOverflowVisible.getValue()) {
        document.body.style.overflowY = "visible";
        this.setOverflowVisible.next(true);
      }
    }

    if (this.camera) {
      this.renderer.render(this.scene, this.camera);
    }
    window.requestAnimationFrame(() => this.update());
  }
}
