/**
 * Created by froelund on 5/29/14.
 */
var app = angular.module("draw", []);

app.factory('DrawingCom', function ($http) {
    var sendQueue = [];
    wsPotocol = 'ws://';
    wsHost = window.location.hostname;
    wsPath = window.location.pathname;
    wsPort = ":" + window.location.port;
    if (window.location.protocol == 'https:') {
        wsPotocol = 'wss://';
    }
    if(wsHost == 'draw-froelund.rhcloud.com'){
        if(wsPotocol == 'wss://'){
            wsPort = ':8443';
        }else{
            wsPort = ':8000';
        }
    }else if(wsHost == 'draw.froelund.net'){
        wsPort = ':28001';
        wsPath = '/draw/'
    }
    var wsLocation = wsPotocol + wsHost + wsPort + wsPath + "drawing";
    var ws = new WebSocket(wsLocation);
    this.data = {};
    var updateCB = function (data) {
        console.warn("No callback for new data registered.");
    };
    var sendDataCB = function (data) {
        console.warn("No callback for sending data registered.");
    };
    ws.onopen = function () {
        for (var i = 0; i < sendQueue.length; i++) {
            var objectToSend = sendQueue[i];
            ws.send(objectToSend);
        }
        sendQueue = {
            push: function(data){
                ws.send(data);
            }
        };
    };
    ws.onmessage = function (message) {
        updateCB(message);
    };
    return {
        onUpdate: function (callback) {
            updateCB = callback;
        },
        sendUpdate: function (data) {
            sendQueue.push(data);
        }
    };
});
app.controller('DrawingCtrl', function ($scope, DrawingCom) {
    $scope.drawSource = "Initial";
    $scope.onDrawingUpdate = function(updateData){
        DrawingCom.sendUpdate(JSON.stringify({
            data: updateData
        }))
    }
    DrawingCom.onUpdate(function (message) {
        $scope.drawSource = JSON.parse(message.data);
        $scope.$apply()
    });
});
app.directive('drawing', function () {
    return {
        restrict: "C",
        scope: {
            onDrawingUpdate: '&',
            drawSource: '='
        },
        link: function(scope, element, attrs){
            var drawing = false;
            var canvas = element[0];
            var ctx = canvas.getContext('2d');
            var busyPX = ctx.createImageData(1,1);
            var lastX;
            var lastY;
            var dataCache = {};
            element.bind('mousedown', function(event){
                if(event.offsetX!==undefined){
                    lastX = event.offsetX;
                    lastY = event.offsetY;
                } else {
                    lastX = event.layerX - event.currentTarget.offsetLeft;
                    lastY = event.layerY - event.currentTarget.offsetTop;
                }
                ctx.beginPath();

                drawing = true;
            });
            element.bind('mousemove', function(event){
                if(drawing){
                    var currentX;
                    var currentY;
                    // get current mouse position
                    if(event.offsetX!==undefined){
                        currentX = event.offsetX;
                        currentY = event.offsetY;
                    } else {
                        currentX = event.layerX - event.currentTarget.offsetLeft;
                        currentY = event.layerY - event.currentTarget.offsetTop;
                    }

                    draw(lastX, lastY, currentX, currentY);

                    // set current coordinates to last one
                    lastX = currentX;
                    lastY = currentY;
                }

            });
            element.bind('mouseup', stopDraw);
            element.bind('mouseleave', stopDraw);

            function stopDraw(event){
                drawing = false;
                var image = ctx.getImageData(0, 0, canvas.width, canvas.height);
                var cleanImage = {};
                var diffData = {};

                for (var i = 0; i < image.data.length; i+=4) {
                    var x = (Math.floor(i / 4) % canvas.width);
                    var y = Math.floor(Math.floor(i/canvas.width)/4);
                    if (image.data[i + 3] > 0) {
                        cleanImage[x + "," + y] = {
                            r: image.data[i],
                            g: image.data[i + 1],
                            b: image.data[i + 2],
                            transparency: image.data[i + 3]
                        };
                    }
                };

                diffData = diffImageData(cleanImage, dataCache)

                scope.onDrawingUpdate({
                    updateData: diffData,
                    context: ctx
                });
            }
            function reset(){
                element[0].width = element[0].width;
            }
            function draw(lX, lY, cX, cY){
                ctx.moveTo(lX,lY);
                ctx.lineTo(cX,cY);
                ctx.strokeStyle = "#4bf";
                ctx.stroke();
            }
            function diffImageData(mainData, lookupData){
                var diffData = {};
                for(var coordinate in mainData){
                    if(coordinate in lookupData){
                        if(JSON.stringify(lookupData[coordinate]) != JSON.stringify(mainData[coordinate])){
                            diffData[coordinate] = mainData[coordinate];
                        }
                    }else{
                        diffData[coordinate] = mainData[coordinate]
                    }
                    lookupData[coordinate] = mainData[coordinate];
                }
                return diffData;
            }
            scope.$watch('drawSource', function(newSource, oldSource){
                var diffData = diffImageData(newSource.data, dataCache);
                for (var coordinate in diffData) {
                    var colorData = diffData[coordinate];
                    coordinate = coordinate.split(",");
                    busyPX.data[0] = colorData.r;
                    busyPX.data[1] = colorData.g;
                    busyPX.data[2] = colorData.b;
                    busyPX.data[3] = colorData.transparency;
                    ctx.putImageData( busyPX, coordinate[0], coordinate[1] );
                }
            }, true);
        }
    };
});
