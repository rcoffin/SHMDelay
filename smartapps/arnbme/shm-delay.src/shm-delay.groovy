 /*
 *  Smart Home Entry and Exit Delay and Open Contact Monitor, Parent 
 *  Functions: 
 *		Acts as a container/controller for Child module
 * 
 *  Copyright 2017 Arn Burkhoff
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *	Aug 17, 2017 v1.0.5  Revise documentation prior to release
 *	Aug 14, 2017 v1.0.4  Revise documentation for exit delay, split about page into about and installation pages
 *	Aug 14, 2017 v1.0.3  Revise initial setup testing app.getInstallationState() for COMPLETE vs childApps.size
 *					done in v1.0.1
 *	Aug 13, 2017 v1.0.2  Add documentation pages (Thanks to Stephan Hackett Button Controller)
 *	Aug 12, 2017 v1.0.1  Add warning on initial setup to install first (Thanks to E Thayer Lock Manager code) 
 *	Aug 11, 2017 v1.0.0  Create from example in developer documentation 
 *
 */

definition(
    name: "SHM Delay",
    namespace: "arnbme",
    author: "Arn Burkhoff",
    description: "SmartApp simulating missing entry and exit delay option in SmartHome",
    category: "My Apps",
    iconUrl: "https://www.arnb.org/IMAGES/hourglass.png",
    iconX2Url: "https://www.arnb.org/IMAGES/hourglass@2x.png",
    iconX3Url: "https://www.arnb.org/IMAGES/hourglass@2x.png",
    singleInstance: true)

preferences {
    page(name: "main")
    page(name: "aboutPage", nextPage: "installPage")
    page(name: "installPage", nextPage: "delayPage")
    page(name: "delayPage", nextPage: "createSimPage")
    page(name: "createSimPage", nextPage: "main")
}

def main()
	{
	dynamicPage(name: "main", title: "Delay Simulator for SmartHome", install: true, uninstall: true)
		{
//		if (childApps?.size())
//		log.debug "${app.getInstallationState()}"
		if (app.getInstallationState() == 'COMPLETE')	//note documentation shows as lower case, but returns upper
			{  
			section 
				{
				app(name: "EntryDelayProfile", appName: "SHM Delay Child", namespace: "arnbme", title: "Create A New Delay Profile", multiple: true)
				}
			}
		else	{
			section 
				{
				paragraph "Please read the documentation, then complete the install by clicking 'Done' above before creating your first profile"
//				app(name: "EntryDelayProfile", appName: "Shm Delay Child", namespace: "arnbme", title: "Create A New Delay Profile", multiple: true)
				}
			}
		section
			{
			href(name: "href",
			title: "Introduction",
			required: false,
			page: "aboutPage")
			}
		section
			{
			href(name: "href",
			title: "Installation and Prerequisites",
			required: false,
			page: "installPage")
			}
		section
			{
			href(name: "href",
			title: "How to create a Delay Profile",
			required: false,
			page: "delayPage")
			}
		section
			{
			href(name: "href",
			title: "How to create a simulated contact sensor",
			required: false,
			page: "createSimPage")
			}
		}
	}

def aboutPage()
	{
	dynamicPage(name: "aboutPage", title: "Introduction")
		{
		section 
			{
			paragraph "This smartapp simulates the Entry and Exit Delay parameters currently missing in SmartHome, "+
			"giving you some time on entry to disarm the system using any method you choose prior to triggering an intrusion alert, "+
			"and does not trigger an intrusion when a monitored contact sensor opens during the exit delay time."+
			" It also replaces the 'Open contact when system is armed' monitor for the monitored contact, lost when removing the contact sensor from SmartHome.\n\n"+
 			"***Please Note***: SmartHome is fully armed during operation of this SmartApp. Tripping a non-simulated sensor"+ 
 			" immediately triggers an intrusion alert."
			
			}
		}
	}

def installPage()
	{
	dynamicPage(name: "installPage", title: "Installation and Prerequisites")
		{
		section
			{
			paragraph "Installation:\n"+
			"Please complete the install by clicking 'Done' on the main page before creating your first profile.\n\n"+
			"Prerequisites:\n"+
			"  1. For each contact sensor to be monitored, create a simulated contact sensor in the IDE. "+
			"The name of this device is used by SmartHome on intrusion alert messages.\n"+
			"  2. Remove the real contact sensor from SmartHome security monitoring. "+
			"This app monitors this device and includes a replacement for the SmartHome 'system armed with open contact' monitor.\n"+
			"  3. Add the simulated contact sensor to SmartHome security monitoring. "+
			"This app 'opens' this device, creating a SmartHome intrusion alert."
			}
		}
	}

def delayPage()
	{
	dynamicPage(name: "delayPage", title: "Creating a Delay Profile")
		{
		section
			{
			paragraph "Define a profile for each real contact sensor removed from SmartHome's monitoring, "+
			"pairing it with a simulated contact sensor.\n"+ 
			"Start by tapping 'Create A New Delay Profile'\n"+
			"  1. select a real contact sensor\n"+
			"  2. select a simulated contact device\n"+
			"  3. Profile name is internally set to Profile: (real contact sensor name) It may be modified\n"+
			"  4. Tap 'Next' on top of page\n\n"+
			"The 'Entry and Exit Data' page displays\n"+
			"  1. set the entry delay time in seconds from 0 to 60. Default:30\n"+
			" (zero is allowed enabling this app to be used only as an exit delay)\n"+
			"  2. For away mode set the exit delay time in seconds from 0 to 60. Default:30\n"+
			" (When using the lock-manager exit delay, or any other exit delay method, set exit delay to 0)\n"+
			" (Exit delay is not active on Stay or Night mode)\n"+
			"  3. (Optional) set keypads where to sound the entrydelay tones\n"+
			"  4. (Optional) set sirens where beep should be issued\n"+
			"  5. Tap 'Next' on top of page\n\n"+
			"The 'Monitor: SmartHome changed to armed and this contact is open' page displays\n"+
			"  1. set the maximum number of warning messages. Default: 2, Minimum: 1\n"+
			"  2. set the number of minutes between messages from 1 to 15. Default: 1\n"+
			"  3. set if the open door message should show in the notifications log. Default: true\n"+
			"  4. set if the open door message should be issued as an application notification. Default: true\n"+
			"  5. set if the open door message should be sent by text (SMS) message. Default: false\n"+
			"  Enter telephone number. Separate multiple numbers with a semicolon(;)\n"+
			"  6. Tap 'Done' on top of page\n\n"
			}
		}
	}

def createSimPage()
	{
	dynamicPage(name: "createSimPage", title: "How to Create A Simulated Contact Sensor")
		{
		section 
			{
			href title: "1. Go to graph.api.smartthings.com", style: "external", 
				url: "https://graph.api.smartthings.com"
			paragraph " 2. Login\n"+
			" 3. Click 'My Locations'\n"+
			" 4. Under 'Name' click on the named location\n"+
			" (The URL changes to the server name servicing this location. Smarthings calls this a shard)\n"+
			" 5. On top menu: Click 'My Devices'\n"+ 
			" 6. Click the 'New Device' button\n"+
			" 7. Name: this can be whatever you want. Suggest adding 'SIM ' to the beginning of the real device's name. For example: SIM Front Door Sensor.\n"+ 
			" 8. Label: this is optional and can be whatever you want.\n"+ 
			" 9. Device Network Id: This can be anything you want, but it cannot duplicate other device ID's. For example SIM01.\n"+
			"10. Zigbee Id: leave blank\n"+ 
			"11. Type: from the dropdown select 'Simulated Contact Sensor'\n"+
			"12. Version: select 'Published'\n"+
			"13. Location: from the dropdown select your location name\n"+ 
			"14. Hub: from the dropdown select your hub name.\n"+ 
			"15. Group: you won't be able to select when creating, but these are Groups you may have created in the Things page in the SmartThings app.\n"+ 
			"16. Click Create" 
			}
		}
	}


def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}

def initialize() {
    // nothing needed here, since the child apps will handle preferences/subscriptions
    // this just logs some messages for demo/information purposes
    log.debug "there are ${childApps.size()} child smartapps"
    childApps.each {child ->
        log.debug "child app: ${child.label}"
    }
} 