<template>
  <div>
    <div class="bg-overlay-custom"></div>
    <div id="map-container" class="map-container"></div>
    <div id="coachDetail">
      <ul class="list-group list-group-flush">
        <h5> 车厢信息 </h5>
        <li class="list-group-item">车厢编号： {{ currentCoach.id }}</li>
        <li class="list-group-item">车厢司机： {{ currentCoach.user }}</li>
        <li class="list-group-item">车厢型号： {{ currentCoach.type }}</li>
        <li class="list-group-item">车厢坐标： {{ currentCoach.coordinate }}</li>
        <li class="list-group-item">车厢来自： {{ currentCoach.from }}</li>
        <li class="list-group-item">车厢去往： {{ currentCoach.to }}</li>
        <li class="list-group-item">车厢油量： {{ currentCoach.oil }}</li>
        <li class="list-group-item">车厢温度： {{ currentCoach.temperature }}</li>
        <li class="list-group-item">车厢湿度： {{ currentCoach.humidity }}</li>
        <li class="list-group-item">车厢压力： {{ currentCoach.pressure }}</li>
      </ul>
    </div>

    <div id="mapControl">
      <h5> 地图控制 </h5>
      <div class="row">
        <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
          <input type="radio" class="btn-check" name="trackRadio" id="trackRadio1" autocomplete="off">
          <label class="btn btn-outline-primary" for="trackRadio1" @click="showTrack('now')"> 当前轨迹 </label>

          <input type="radio" class="btn-check" name="trackRadio" id="trackRadio2" autocomplete="off">
          <label class="btn btn-outline-primary" for="trackRadio2" @click="showTrack('history')"> 历史轨迹 </label>
        </div>
      </div>


<!--      <div class="row">-->
<!--        <div class="btn-group" role="group" aria-label="Basic radio toggle button group">-->
<!--          <input type="radio" class="btn-check" name="mapRadio" id="mapRadio1" autocomplete="off">-->
<!--          <label class="btn btn-outline-primary" for="mapRadio1"> 默认 </label>-->

<!--          <input type="radio" class="btn-check" name="mapRadio" id="mapRadio2" autocomplete="off">-->
<!--          <label class="btn btn-outline-primary" for="mapRadio2" @click="change_img()"> 影像 </label>-->

<!--          <input type="radio" class="btn-check" name="mapRadio" id="mapRadio3" autocomplete="off">-->
<!--          <label class="btn btn-outline-primary" for="mapRadio3" @click="change_vec()"> 街道 </label>-->

<!--          <input type="radio" class="btn-check" name="mapRadio" id="mapRadio4" autocomplete="off">-->
<!--          <label class="btn btn-outline-primary" for="mapRadio4" @click="change_ter()"> 地形 </label>-->
<!--        </div>-->
<!--      </div>-->
    </div>
  </div>
</template>

<script>
import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import {fromLonLat, toLonLat} from 'ol/proj'
import Point from 'ol/geom/Point';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import Feature from 'ol/Feature';
import * as turf from '@turf/turf'
import GeoJSON from 'ol/format/GeoJSON'
import {getVectorContext} from 'ol/render';
import Style from 'ol/style/Style';
import Stroke from 'ol/style/Stroke';
import Fill from 'ol/style/Fill';
import CircleStyle from 'ol/style/Circle';
import icon from '../assets/images/location.jpg';
import Icon from "ol/style/Icon";
import request from "@/assets/js/request";
import storage from "@/assets/js/storage";

export default {
  name: "OlMap",
  data() {
    return {
      map: new Map(null),
      tileLayer: new TileLayer({
        source: new XYZ({
          url: 'https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png'
        })
      }),

      arcLinesFeature: [],
      iconFeatures: [],
      turfFormat: new GeoJSON(),
      arcStyle: new Style({
        stroke: new Stroke({
          color: "#99CC66",
          width: 3
        })
      }),
      dotStyle: new Style({
        image: new CircleStyle({
          fill: new Fill({
            color: "#FF6666"
          }),
          radius: 5
        })
      }),
      currentCoach: {
        id: "",
        user: "",
        type: "",
        coordinate: "",
        from: "",
        to: "",
        oil: "",
        temperature: "",
        humidity: "",
        pressure: ""
      }
    }
  },
  methods: {
    initMap: function (center, zoom) {
      let view = new View({
        center: center,
        zoom: zoom,
        maxZoom: 15,
        minZoom: 4,
      });
      return new Map({
        target: 'map-container',
        controls: [],
        layers: [
          this.tileLayer
        ],
        view: view
      });
    },
    getTurfArcFeature: function (start, end, opt) {
      let line = turf.lineString([
        start,
        [start[0] + (end[0] - start[0]) * 0.5,
          start[1] + (end[1] - start[1]) * 0.65],
        end
      ]);
      let curved = turf.bezierSpline(line);
      let length = turf.length(curved, {units: 'meters'});
      let bF = this.turfFormat.readFeature(curved);
      bF.getGeometry().transform('EPSG:4326', 'EPSG:3857');
      bF.setProperties(opt);
      bF.set("length", length);
      return bF;
    },
    addFeature: function (trackType) {
      request.get("/api/server/coach/all").then(res => {
        if (res) {
          this.initFeatureData();

          res.forEach((item) => {
            let fromCoordStrings = item.fromCoord.split(",");
            let fromCoordArray = [parseFloat(fromCoordStrings[0]), parseFloat(fromCoordStrings[1])];
            let toCoordStrings = item.toCoord.split(",");
            let toCoordArray = [parseFloat(toCoordStrings[0]), parseFloat(toCoordStrings[1])];
            let currentCoordStrings = item.currentCoord.split(",");
            let currentCoordArray = [parseFloat(currentCoordStrings[0]), parseFloat(currentCoordStrings[1])];

            if (trackType === "now") {
              let tempF = this.getTurfArcFeature(currentCoordArray, toCoordArray, {
                'from': item.fromName,
                'to': item.toName
              });
              this.arcLinesFeature.push(tempF);
            } else {
              let tempF = this.getTurfArcFeature(fromCoordArray, currentCoordArray, {
                'from': item.fromName,
                'to': item.toName
              });
              this.arcLinesFeature.push(tempF);
            }

            let iconFeature = new Feature({
              id: item.id,
              from: item.fromName,
              to: item.toName,
              oil: item.oil,
              pressure: item.pressure,
              humidity: item.humidity,
              temperature: item.temperature,
              user: item.username,
              type: item.type,
            });
            this.iconFeatures.push(iconFeature);
          });


          let trainLayer = new VectorLayer(
              {
                source: new VectorSource({
                  features: this.arcLinesFeature
                })
              }
          );
          this.map.addLayer(trainLayer);

          let iconLayer = new VectorLayer(
              {
                source: new VectorSource({
                  features: this.iconFeatures
                })
              }
          );
          this.map.addLayer(iconLayer);
        }
      })
    },
    initFeatureData: function () {
      // 清理图层
      let currentLayers = this.map.getLayers()['array_'];
      let layerCount = this.map.getLayers()['array_'].length;
      if (layerCount > 1) {
        this.map.removeLayer(currentLayers[layerCount - 1]);
      }

      // 清理点
      this.arcLinesFeature = [];
      this.iconFeatures = [];
    },
    onRender: function () {
      this.tileLayer.on('postrender', (evt) => {
        let veContext = getVectorContext(evt);
        this.arcLinesFeature.forEach((item, index) => {
          veContext.drawFeature(item, this.arcStyle);
          let time = (evt.frameState.time - item.get('start')) / 20000;
          let frac = time / 5 - index / this.arcLinesFeature.length;
          if (!item.get('start')) item.set('start', new Date().getTime());
          if (frac >= 1) {
            item.set('start', new Date().getTime());
            frac = 0;
          }
          let along = item.getGeometry().getCoordinateAt(frac);
          // let pF = new Feature(new Point(along));
          // veContext.drawFeature(pF, this.dotStyle);

          let iconFeature = this.iconFeatures[index];
          iconFeature.setGeometry(new Point(along));
          let iconStyle = new Style({
            image: new Icon({
              anchor: [0.5, 0.5],
              anchorOrigin: 'top',
              src: icon,
              scale: 0.15
            })
          });
          iconFeature.setStyle(iconStyle);
        })
        this.map.render()
      })
    },
    flyTo: function (location, done) {
      let duration = 2000;
      let zoom = 7;
      let parts = 2;
      let called = false;

      function callback(complete) {
        --parts;
        if (called) {
          return;
        }
        if (parts === 0 || !complete) {
          called = true;
          done(complete);
        }
      }

      this.map.getView().animate({
            center: location,
            duration: duration,
          },
          callback
      );
      this.map.getView().animate({
            zoom: zoom - 1,
            duration: duration / 2,
          }, {
            zoom: zoom,
            duration: duration / 2,
          },
          callback
      );
    },
    addSelectListener: function () {
      this.map.on('click', (evt) => {
          const coordinate = evt.coordinate;
          const lonLat = toLonLat(coordinate);
          alert(lonLat);

          let pixel = this.map.getEventPixel(evt.originalEvent);
          let feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
                  return feature;
              }
          );
          if (feature && feature.getProperties()['id']) {
              let coordinate = feature.getProperties()['geometry']['flatCoordinates'];
              this.flyTo(coordinate, function () {
              });

              this.currentCoach = {
                id: feature.getProperties()['id'],
                coordinate: coordinate,
                from: feature.getProperties()['from'],
                to: feature.getProperties()['to'],
                oil: feature.getProperties()['oil'],
                pressure: feature.getProperties()['pressure'],
                humidity: feature.getProperties()['humidity'],
                user: feature.getProperties()['user'],
                type: feature.getProperties()['type'],
                temperature: feature.getProperties()['temperature'],
              }
            } else {
              this.currentCoach = {
                id: "",
                user: "",
                type: "",
                coordinate: "",
                from: "",
                to: "",
                oil: "",
                temperature: "",
                humidity: "",
                pressure: ""
              }
            }
          }
      )
    },
    showTrack: function (trackType) {
        let username = storage.getItem("username");
        if (username === false) {
            alert("请登录查看");
        } else {
            this.addFeature(trackType);
        }
    },
    change_img() {
      var img = new TileLayer({
        source: new XYZ({
          url: 'http://t3.tianditu.com/DataServer?T=img_w&x={x}&y={y}&l={z}&tk=d0cf74b31931aab68af181d23fa23d8d'
        })
      });
      this.map.addLayer(img)
    },
    change_vec() {
      var map_cva = new TileLayer({
        source: new XYZ({
          url: "http://t3.tianditu.com/DataServer?T=cva_w&x={x}&y={y}&l={z}&tk=d0cf74b31931aab68af181d23fa23d8d"
        })
      });
      var map_vec = new TileLayer({
        source: new XYZ({
          url: "http://t4.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}&tk=d0cf74b31931aab68af181d23fa23d8d"
        })
      });

      this.map.addLayer(map_vec);
      this.map.addLayer(map_cva);
      //console.log(this.map.getLayers());
    },
    change_ter() {
      var map_ter = new TileLayer({
        source: new XYZ({
          url: "http://t4.tianditu.com/DataServer?T=ter_w&x={x}&y={y}&l={z}&tk=d0cf74b31931aab68af181d23fa23d8d"
        })
      });
      var map_cta = new TileLayer({
        source: new XYZ({
          url: "http://t4.tianditu.com/DataServer?T=cva_w&x={x}&y={y}&l={z}&tk=d0cf74b31931aab68af181d23fa23d8d"
        })
      });
      this.map.addLayer(map_ter);
      this.map.addLayer(map_cta);
    }
  },
  mounted() {
      // this.map = this.initMap(fromLonLat([105.74982533319637, 37.71602799673174]), 4.7);
      this.map = this.initMap(fromLonLat([116.52616976895676,39.991732847489686]), 15);

    this.onRender();

    this.addSelectListener();
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.map-container {
  width: 100%;
  height: 900px;
}

#map-container:focus {
  outline: #4A74A8 solid 0.15em;
}

#coachDetail {
  position: absolute;
  top: 4.5em;
  left: .5em;
}

#mapControl {
  position: absolute;
  top: 4.5em;
  right: .5em;
}
</style>