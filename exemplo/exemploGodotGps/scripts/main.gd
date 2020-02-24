extends Control

var colorOff = Color.red
var colorOn = Color.green
var linkGoogleMaps = "https://www.google.com/maps/place/[LAT],[LNG]/@[LAT],[LNG],19z"
var gps_tracker = null
var using = false
var count = 0

func _ready():
	_resetLabels()
	$btnGoogleMaps.hide()
	OS.request_permissions()
	
	if(Engine.has_singleton("GodotGps")):
		gps_tracker = Engine.get_singleton("GodotGps")
		if gps_tracker != null:
			$vbox/init/label.text = "here is"
			#$btnTrack.text = "Tracking..."
			#gps_tracker.getInit()
			#using = true
			
			_resetLabels()
			_checkGpsStatus()

func _process(delta):
	if !using:
		$vbox/init/label.text = str("stoped: ", count)
		return
		

	if gps_tracker != null:
		count = count + 1
		$vbox/init/label.text = str(count)
		$vbox/latitude/label.text = str("latitude: ", gps_tracker.getStringLatitude())
		$vbox/longitude/label.text = str("longitude: ", gps_tracker.getStringLongitude())
		$vbox/altitude/label.text = str("altitude: ", gps_tracker.getStringAltitude())
		$vbox/gpstime/label.text = str("time: ", gps_tracker.getStringGPSTime())
		$vbox/speed/label.text = str("speed: ", gps_tracker.getStringGPSSpeed())
		$vbox/bearing/label.text = str("bearing: ", gps_tracker.getStringGPSBearing())
		$vbox/accuracy/label.text = str("accuracy: ", gps_tracker.getStringGPSAccuracy())
		_checkGpsStatus()
	else:
		_resetLabels()

func _on_btnTrack_pressed():
	if gps_tracker != null:
		if using:
			gps_tracker.stopUsingGPS()
			using = false
			$btnTrack.text = "Track"
			$btnGoogleMaps.hide()
		else:
			gps_tracker.getInit()
			$btnTrack.text = "Tracking..."
			$btnGoogleMaps.show()
			using = true
			count = 0
	_resetLabels()
	_checkGpsStatus()

func _checkGpsStatus():
	$ColorRect/nodeNetwork/led.color = colorOff
	$ColorRect/nodeGps/led.color = colorOff
	
	if gps_tracker != null:
		var resG = int(gps_tracker.getGPSState())
		var resN = int(gps_tracker.getNetworkState())
		
		if resG == 1 :
			$ColorRect/nodeGps/led.color = colorOn
		if resN == 1 :
			$ColorRect/nodeNetwork/led.color = colorOn

func _resetLabels():
	$vbox/latitude/label.text = str("latitude: ", "...")
	$vbox/longitude/label.text = str("longitude: ", "...")
	$vbox/altitude/label.text = str("altitude: ", "...")
	$vbox/gpstime/label.text = str("time: ", "...")
	$vbox/speed/label.text = str("speed: ", "...")
	$vbox/bearing/label.text = str("bearing: ", "...")
	$vbox/accuracy/label.text = str("accuracy: ", "...")

func _on_btnGoogleMaps_pressed():
	if gps_tracker != null:
		var lat = gps_tracker.getStringLatitude()
		var lng = gps_tracker.getStringLongitude()
		var url = linkGoogleMaps
		url = url.replace("[LAT]", lat)
		url = url.replace("[LNG]", lng)
		
		OS.shell_open(url)
		prints(url)
