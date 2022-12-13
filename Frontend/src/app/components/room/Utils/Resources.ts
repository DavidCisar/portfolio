import { EventEmitter } from 'events';
import Assets from './assets';
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import { DRACOLoader } from 'three/examples/jsm/loaders/DRACOLoader.js';

export default class Resources extends EventEmitter {

  public assets = Assets;
  public items: any[] = [];
  public queue: any;
  public loaded: number;
  public loaders: any;

  public video: any;
  public videoTexture: any;

  constructor() {
    super();
    this.queue = this.assets.length;
    this.loaded = 0;

    this.setLoaders();
    this.startLoading();
  }

  setLoaders() {
    this.loaders = {};
    this.loaders.gltfLoader = new GLTFLoader();
    this.loaders.dracoLoader = new DRACOLoader();
    this.loaders.dracoLoader.setDecoderPath('./assets/three/draco/');
    this.loaders.gltfLoader.setDRACOLoader(this.loaders.dracoLoader);
  }

  startLoading() {
    for (const asset of this.assets) {
      if (asset.type === 'glbModel') {
        this.loaders.gltfLoader.load(asset.path, (file: any) => {
          this.singleAssetLoaded(asset.name, file);
        })
      } else if (asset.type === 'videoTexture') {
      console.log("is video texture")
        this.video = document.createElement('video');
        this.video.src = asset.path;
        this.video.playsInLine = true;
        this.video.muted = true;
        this.video.autoplay = true;
        this.video.loop = true;
        this.video.play();

        this.videoTexture = new THREE.VideoTexture(this.video);
        this.videoTexture.flipY = true;
        this.videoTexture.minFilter = THREE.NearestFilter;
        this.videoTexture.magFilter = THREE.NearestFilter;
        this.videoTexture.generateMipMaps = false;
        this.videoTexture.encoding = THREE.sRGBEncoding;

        this.singleAssetLoaded(asset.name, this.videoTexture);
      }
    }
  }

  singleAssetLoaded(name: string, file: any) {
    let item: {name: string, file: any} = { "name": name, "file": file}
    this.items.push(item);
    this.loaded++;

    if (this.loaded === this.queue) {
      this.emit("ready")
    }
  }

  getRoom() {
    for (const item of this.items) {
      if (item.name === "room") {
        return item.file;
      }
    }
  }

  getTrees() {
    for (const item of this.items) {
      if (item.name === "trees") {
        return item.file;
      }
    }
  }

  getVideo() {
    return this.videoTexture;
  }
}
