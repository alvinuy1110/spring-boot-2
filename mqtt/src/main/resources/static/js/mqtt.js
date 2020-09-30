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
  client = new Paho.MQTT.Client(host, Number(port), clientID);

  // Set callback handlers
  client.onConnectionLost = onConnectionLost;
  client.onMessageArrived = onMessageArrived;

  // Connect the client, if successful, call onConnect function
  client.connect({
    onSuccess: onConnect,
  });
}


function publish() {

  // Fetch the MQTT topic from the form
  topic = document.getElementById("pubTopic").value;
  message = document.getElementById("pubMessage").value;

  // Print output for the user in the messages div
  document.getElementById("messages").innerHTML += '<span>Publishing to: ' + topic + ' with message -> '+ message + '</span><br/>';

  // qos, retain
  client.send(topic,message, 2, true);

  updateScroll(); // Scroll to bottom of window
}


function subscribe() {

  // Fetch the MQTT topic from the form
  topic = document.getElementById("subTopic").value;

  // Print output for the user in the messages div
  document.getElementById("messages").innerHTML += '<span>Subscribing to: ' + topic + '</span><br/>';

  // put settings
  
  client.subscribe(topic, {qos: 2});
  updateScroll(); // Scroll to bottom of window
}

function unsubscribe() {

  // Fetch the MQTT topic from the form
  topic = document.getElementById("subTopic").value;

  // Print output for the user in the messages div
  document.getElementById("messages").innerHTML += '<span>Unsubscribing to: ' + topic + '</span><br/>';

  // put settings

  client.unsubscribe(topic);
  updateScroll(); // Scroll to bottom of window
}
// Called when the client connects
function onConnect() {
  // // Fetch the MQTT topic from the form
  // topic = document.getElementById("topic").value;
  //
  // // Print output for the user in the messages div
  // document.getElementById("messages").innerHTML += '<span>Subscribing to: ' + topic + '</span><br/>';
  //
  // // Subscribe to the requested topic
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
  document.getElementById("messages").innerHTML += '<span>Incoming message from Topic: ' + message.destinationName + '  | ' + message.payloadString + '</span><br/>';
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