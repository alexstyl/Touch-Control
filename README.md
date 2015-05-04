# Touch-Control
An overlay that sits on top of all other applications, allowing the user to open apps and perform various actions no matter the app they are in. 



Features:
<ul>
<li>Control the app with touch and physical gestures.</li>
<li>The user can draw a gesture, after triple pressing the overlay. Long pressing it starts a voice command.</li>
<li>Voice commands are "Call x" (which calls the contact x) or "Open x" (which opens the app x)</li>
<li>The overlay appears only if the app is on the background.</li>
<li>While the app is in the background, the service runs on foreground with low priority notification. That way, the app is not going to be terminated by the system, and also the foreground notification does not get into the user's way.</li>
<li>If the devices goes to low battery, the service's sensors are turned off. That means that the user cannot operate the physical gestures. When the battery goes into normal battery (or is connected to a plug) it is turned on again.</li>
<li>The user can create their own touch gestures. They can select between 6 different actions to perform (Call number, call contact, checkin via foursquare, navigate to, open app and increase/decrease volume).</li>
<li>The user can flip their device to mute notifications, shake their device to turn the torch on. In dark places, the user gets a notification that asks the user whether they like to turn the torch on.</li>
<li>All physical gestures can be disabled.</li>


<li>In order to preserve battery, the sensors are being unregistered when the screen is off, and are being registered when the screen is on again.</li>
<li>Two locales: English, Greek.</li>
</ul>

<p>This app was developed as part of one of the modules of my MSc in Human-Computer Interaction.</p>
