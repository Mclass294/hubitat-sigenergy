# Sigenergy Plant Driver for Hubitat

A native Hubitat driver providing **local Modbus TCP integration** with Sigenergy Energy Controllers and Gateways.

The driver communicates directly with the Sigenergy system over the local network without requiring cloud services, Home Assistant or MQTT.

---

## Features

* Local Modbus TCP communication
* No cloud dependency
* Configurable polling interval
* Individual measurement selection
* Numeric attributes for Rule Machine compatibility
* Automatic socket reconnect
* Multi-slave Modbus support
* Poll statistics and error reporting
* Community open-source project

---

## Supported Hardware

Developed and tested with:

* Sigenergy Single Phase 5kW Energy Controller
* Sigenergy Gateway
* Sigenergy Battery System

Protocol:

* Sigenergy Modbus Protocol V2.9

Other Sigenergy products may also work if they support the same Modbus register set.

---

## Measurements

The driver supports the following measurements:

| Measurement                    | Register |
| ------------------------------ | -------: |
| EMS Mode                       |    30003 |
| Grid Sensor Status             |    30004 |
| Grid Power                     |    30005 |
| On/Off Grid Status             |    30009 |
| Battery State of Charge        |    30014 |
| Plant Power                    |    30031 |
| Solar Power                    |    30035 |
| Battery Power                  |    30037 |
| Third Party PV Power           |    30194 |
| Total Load Power               |    30284 |
| Plant Running State            |    30051 |
| Daily Export Energy            |    30554 |
| Daily Import Energy            |    30560 |
| Battery Daily Charge Energy    |    30566 |
| Battery Daily Discharge Energy |    30572 |
| Available Charge Energy        |    30595 |
| Available Discharge Energy     |    30597 |
| Battery State of Health        |    30602 |
| Battery Temperature            |    30603 |
| Grid Frequency                 |    31002 |
| Phase A Voltage                |    31011 |
| Phase A Current                |    31017 |
| Power Factor                   |    31023 |

---

## Requirements

* Hubitat Elevation hub
* Sigenergy system with Modbus TCP enabled
* Local IP connectivity between Hubitat and the Sigenergy controller

---

## Installation

1. Download `SigenergyPlantDriver.groovy`

2. Open Hubitat.

3. Navigate to:

   **Drivers Code**

4. Create a **New Driver**.

5. Paste the driver source.

6. Click **Save**.

7. Click **Compile**.

Detailed installation instructions are provided in **docs/Installation.md**.

---

## Configuration

Configure:

* IP Address
* TCP Port (normally 502)
* Battery Slave ID
* Poll Interval
* Measurements to monitor

After saving preferences, click **Initialize** if required.

---

## Current States

All measurement values are exposed as **numeric attributes** making them suitable for:

* Rule Machine
* Dashboards
* Custom Apps
* Logging
* Automations

---

## Troubleshooting

The driver exposes several diagnostic attributes:

* successfulPolls
* failedPolls
* lastError
* lastPollTime
* lastResponseTime

If communication fails:

* Verify Modbus TCP is enabled.
* Verify the IP address.
* Verify TCP port 502 is accessible.
* Ensure Hubitat can reach the Sigenergy controller.

Additional troubleshooting information is available in **docs/Troubleshooting.md**.

---

## Version History

### Version 2.0.0

* Major refactor of the original driver
* Apache License 2.0
* Single register definition table
* Removed obsolete Basic/Custom register profiles
* Removed duplicate polling entries
* Numeric-only measurement attributes
* Improved readability and maintainability

### Version 2.1.0

Renamed driver to a version-independent filename.
Added:
* Grid Frequency
* Phase A Voltage
* Phase A Current
* Power Factor

### Version 2.2.0

Added text status attributes for:
* EMS mode
* Grid sensor status
* Grid connection status
* Plant running state.
* Numeric status attributes remain unchanged for Rule Machine compatibility.

---

## Support
Support is unable to be provided, but documentation has been provided to allow the addition of additional Sigenergy registers as required (see Conributing below)

---

## License

Licensed under the Apache License, Version 2.0.

See the LICENSE file for details.

---

## Contributing

Contributions are invited.
Documents have been provided outlining the structure of the driver (CONTRIBUTING.md).
A second document describes how you can add further registers to meet your needs 

---

## Acknowledgements

Developed for the Hubitat community to provide reliable local integration with Sigenergy energy systems.

Special thanks to members of the Hubitat Community who tested early versions and provided feedback.

