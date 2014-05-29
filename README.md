Draw
=====

Draw application build on JavaEE and Angularjs

#Usage
This will run on any JavaEE 7 compliant server.

I have configured it to run with openshift, but it seems to run kind of slow.

##Frontend

The Angular application consists of 3 components, all included in the [app.js](https://github.com/Froelund/Draw/blob/master/src/main/webapp/resources/draw/js/app.js)

###`DrawingCom` service
Handles the communication with the backend, currently using WebSockets.

###`drawing` directive
Applies drawing capability to a HTML5 canvas

###`DrawingCtrl`
Uses the the DrawingCom to enrich the canvas with data from the backend(and thereby the others users)