import { EventEmitter } from 'events';
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import { DRACOLoader } from 'three/examples/jsm/loaders/DRACOLoader.js';

export default class Resources extends EventEmitter {

  public assets = [{
                      name: 'room_top',
                      type: 'glbModel',
                      path: './assets/three/models/portfolio_v2_room_top.glb'
                    },
                    {
                      name: 'room_center',
                      type: 'glbModel',
                      path: './assets/three/models/portfolio_v2_room_center.glb'
                    },
                    {
                      name: 'room_bottom',
                      type: 'glbModel',
                      path: './assets/three/models/portfolio_v2_room_bottom.glb'
                    }];
  public items: any[] = [];
  public queue: any;
  public loaded: number;
  public loaders: any;

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

  getTopRoom() {
    for (const item of this.items) {
      if (item.name === "room_top") {
        return item.file;
      }
    }
  }

  getCenterRoom() {
    for (const item of this.items) {
      if (item.name === "room_center") {
        return item.file;
      }
    }
  }

  getBottomRoom() {
    for (const item of this.items) {
      if (item.name === "room_bottom") {
        return item.file;
      }
    }
  }
}
