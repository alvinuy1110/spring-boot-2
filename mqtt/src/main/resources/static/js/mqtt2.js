//NOT YET READY
// 
// const consoleAdd = function (text) {
//   const main = document.getElementById('main');
//   main.innerHTML += text + '<br />';
//   main.scrollTop = main.scrollHeight;
// }

// Called after form input is processed
function startConnect() {
  // Generate a random client ID
  clientID = "clientID-" + parseInt(Math.random() * 100);

  // Fetch the hostname/IP address and port number from the form
  host = document.getElementById("host").value;
  port = document.getElementById("port").value;

  // Print output for the user in the messages div
  document.getElementById("messages").innerHTML += '<span>Connecting to: ' + host + ' on port: ' + port + '</span><br/>';
  document.getElementById("messages").innerHTML += '<span>Using the following client value: ' + clientID + '</span><br/>';

  // Initialize new Paho client connection
  
  // settings for mqtt broker
  const url = 'tcp://' +host+':'+port;
  const username = 'guest';
  const password = 'guest';

  // Connecting to MQTT server using websockets
  const client = mqtt.connect(url, { username, password, clientID });


  // client = new Paho.MQTT.Client(host, Number(port), clientID);

  // Set callback handlers


  client.on('error', function (error) {
    console.log('🚨 Error: ' + error);
  });

  client.on('close', function () {
    console.log('Connection has been closed');
  });
  client.on('reconnect', function () {
    console.log('Reconnecting...');
  });

  // On MQTT connection, we subscribe to the "presence" topic and send "Hello MQTT" to it.
  client.on('connect', function () {
    console.log('Connected.');  
  });
  
}

function publish() {

  // Fetch the MQTT topic from the form
  topic = document.getElementById("pubTopic").value;
  message = document.getElementById("pubMessage").value;

  // Print output for the user in the messages div
  document.getElementById("messages").innerHTML += '<span>Publishing to: ' + topic + ' with message -> '+ message + '</span><br/>';

  // qos, retain
  client.subscribe(topic,message, 2, true);
}


function subscribe() {

  // Fetch the MQTT topic from the form
  topic = document.getElementById("subTopic").value;

  // Print output for the user in the messages div
  document.getElementById("messages").innerHTML += '<span>Subscribing to: ' + topic + '</span><br/>';

  // put settings
  payload = {topic: {qos: 2}}
  client.subscribe(payload);
}

function unsubscribe() {

  // Fetch the MQTT topic from the form
  topic = document.getElementById("subTopic").value;

  // Print output for the user in the messages div
  document.getElementById("messages").innerHTML += '<span>Unsubscribing to: ' + topic + '</span><br/>';

  // put settings

  client.unsubscribe(topic);
}
// Called when the client connects
function onConnect() {
  // Fetch the MQTT topic from the form
  // topic = document.getElementById("topic").value;

  // Print output for the user in the messages div
  // document.getElementById("messages").innerHTML += '<span>Subscribing to: ' + topic + '</span><br/>';

  // Subscribe to the requested topic
  // client.subscribe(topic);
}

// Called when the client loses its connection
function onConnectionLost(responseObject) {
  console.log("onConnectionLost: Connection Lost");
  if (responseObject.errorCode !== 0) {
    console.log("onConnectionLost: " + responseObject.errorMessage);
  }
}

// Called when a message arrives
function onMessageArrived(message) {
  console.log("onMessageArrived: " + message.payloadString);
  document.getElementById("messages").innerHTML += '<span>Topic: ' + message.destinationName + '  | ' + message.payloadString + '</span><br/>';
  updateScroll(); // Scroll to bottom of window
}

// Called when the disconnection button is pressed
function startDisconnect() {
  client.disconnect();
  document.getElementById("messages").innerHTML += '<span>Disconnected</span><br/>';
  updateScroll(); // Scroll to bottom of window
}

// Updates #messages div to auto-scroll
function updateScroll() {
  var element = document.getElementById("messages");
  element.scrollTop = element.scrollHeight;
}