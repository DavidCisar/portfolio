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
  public introTop = 'Hey I\'m David!';
  public introCharactersTop: string[] = [];
  public introCounterTop = 0;
  public introDoneTop = false;
  public introBottom = 'Welcome to my Portfolio';
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

    let introInterval = setInterval(() => {
      for (let i = 0; i < this.introCounterTop; i++) {
        this.introCharactersTop[i] = this.introTop.charAt(i);
      }
      for (let i = this.introCounterTop; i < this.introTop.length; i++) {
        this.introCharactersTop[i] = Math.random().toString(36).charAt(2);
      }
      if (document.getElementById('hacking-1')) {
        document.getElementById('hacking-1')!.innerText = this.introCharactersTop.join('');
      }

      for (let i = 0; i < this.introCounterBottom; i++) {
        this.introCharactersBottom[i] = this.introBottom.charAt(i);
      }
      for (let i = this.introCounterBottom; i < this.introBottom.length; i++) {
        this.introCharactersBottom[i] = Math.random().toString(36).charAt(2);
      }
      if (document.getElementById('hacking-2')) {
        document.getElementById('hacking-2')!.innerText = this.introCharactersBottom.join('');
      }
    }, 45);

    let introRevealInterval = setInterval(() => {
      if (!this.introDoneTop) {
        this.introCounterTop++;
        if (this.introCounterTop == this.introTop.length) {
          this.introDoneTop = true;
        }
      }
      if (!this.introDoneBottom) {
        this.introCounterBottom++;
        if (this.introCounterBottom == this.introBottom.length) {
          this.introDoneBottom = true;
        }
      }
      if (!this.introDone.getValue() && this.introDoneTop && this.introDoneBottom) {
        this.introDone.next(true);
      }
    }, 100);

    let timeUpdate = setInterval(() => {
      let timeHour = new Date().getHours();
      let timeMinute = new Date().getMinutes();
      let timeSecond = new Date().getSeconds();
      this.lightStrength =
        Math.max(0.3, ((timeHour * 3600 + timeMinute * 60 + timeSecond - 43200) * (timeHour * 3600 + timeMinute * 60 + timeSecond - 43200) * (-1) + (86400 * 86400)) / (86400 * 86400));

      this.timeHour = timeHour.toString();
      this.timeMinute = timeMinute.toString();
      this.timeSecond = timeSecond.toString();

      if (this.timeHour.length == 1) {
        this.timeHour = '0' + this.timeHour;
      };
      if (this.timeMinute.length == 1) {
        this.timeMinute = '0' + this.timeMinute;
      };
      if (this.timeSecond.length == 1) {
        this.timeSecond = '0' + this.timeSecond;
      };
    }, 1000);
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
      let exploreElement = document.getElementById('menu');
      let arrow = document.getElementById('arrow');

      let y = window.scrollY;

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
            exploreElement.classList.add('disappear');
            arrow.classList.add('disappear');
            this.scrolledFar.next(true);
          }
        } else {
          if (this.scrolledFar.getValue()) {
            exploreElement.classList.remove('disappear');
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

  async exploreMore() {
    document.body.style.overflowY = "hidden";
    this.compactRooms.next(false);
    let exploreElement = document.getElementById('menu');
    if (exploreElement != null) {
      exploreElement.classList.add('hidden');
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

    let menu = document.getElementById("menu")
    if (menu) {
      menu.style["animationDuration"] = '2s';
    }

    await this.delay(2000);
    this.onExplore.next(true);
  }

  async exploreAbout() {
    document.body.style.overflowY = "hidden";
    this.compactRooms.next(false);
    let exploreElement = document.getElementById('menu');
    if (exploreElement != null) {
      exploreElement.classList.add('hidden');
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

    let menu = document.getElementById("menu")
    if (menu) {
      menu.style["animationDuration"] = '2s';
    }

    await this.delay(2000);
    this.onExplore.next(true);
  }

  async exploreTech() {
    document.body.style.overflowY = "hidden";
    this.compactRooms.next(false);
    let exploreElement = document.getElementById('menu');
    if (exploreElement != null) {
      exploreElement.classList.add('hidden');
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

    let menu = document.getElementById("menu")
    if (menu) {
      menu.style["animationDuration"] = '2s';
    }

    await this.delay(2000);
    this.onExplore.next(true);
  }

  async goBack() {
    if (this.lookingAtPortfolio.getValue()) {
      this.setPath(0, 5, 7.5, 2, this.perspectiveCamera);
      GSAP.timeline().to(this.perspectiveCamera.rotation, {
            x: -0.2,
            y: 0,
            duration: 2
            });
      setTimeout(() => {
        this.lookingAtPortfolio.next(false);
        this.portfolioAll.next(true);
      }, 2000)
    } else {
      this.onExplore.next(false);
      let exploreElement = document.getElementById('menu');
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
    }
    let exploreElement = document.getElementById('menu');
    if (exploreElement != null) {
      exploreElement.classList.remove('hidden');
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
