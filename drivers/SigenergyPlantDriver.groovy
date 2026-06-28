/*
 * Copyright 2026 mclass
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Driver: Sigenergy Plant Driver
 * Version: 2.2.0
 * Author: mclass
 * Reference: Sigenergy Modbus Protocol V2.9
 *
 * Release history:
 * 2.2.0 - Added text status attributes for EMS mode, grid sensor status, grid connection status and plant running state.
 *       - Numeric status attributes remain unchanged for Rule Machine compatibility.
 * 2.1.0 - Added Grid Frequency (31002), Phase A Voltage (31011), Phase A Current (31017) and Power Factor (31023).
 *       - Added optional measurements enabled by default.
 * 2.0.0 - Refactored register polling to a single register definition table.
 *       - Removed obsolete Basic / Custom Register Profile functionality.
 *       - Removed duplicate polling of registers 30194 and 30284.
 *       - Removed display-only attributes and string status display attributes.
 *       - Numeric register attributes now emit numeric values for Rule Machine.
 *       - Measurement points are manually selected in preferences.
 *       - Clears current-state measurement attributes when measurements are disabled.
 * 1.3.0 - Added plant, battery, energy, status metrics and poll statistics.
 */

import groovy.transform.Field

@Field static final String DRIVER_VERSION = "2.2.0 (Protocol V2.9)"
@Field static final Integer PLANT_SLAVE_ID = 247

@Field static final Map<String, String> STATUS_TEXT_ATTRIBUTES = [
    emsMode          : "emsModeText",
    gridSensorStatus : "gridSensorStatusText",
    onOffGridStatus  : "onOffGridStatusText",
    plantRunningState: "plantRunningStateText"
]

@Field static final Map<String, Map<Integer, String>> STATUS_TEXT_LOOKUPS = [
    emsMode: [
        0: "Max self-consumption",
        1: "AI Mode",
        2: "TOU",
        5: "Full Feed-in to Grid",
        7: "Remote EMS mode",
        9: "Custom"
    ],
    gridSensorStatus: [
        0: "Not connected",
        1: "Connected"
    ],
    onOffGridStatus: [
        0: "On grid",
        1: "Off grid (auto)",
        2: "Off grid (manual)"
    ],
    plantRunningState: [
        0: "Standby",
        1: "Running",
        2: "Fault",
        3: "Shutdown",
        7: "Environmental Abnormality"
    ]
]

@Field static final List<Map> REGISTER_DEFINITIONS = [
    [name: "batterySOC",                  title: "Battery State of Charge",  setting: "enableSOC",                   defaultEnabled: true,  slave: "plant",    reg: 30014, count: 1, type: "U16", scale: 10,   unit: "%"],
    [name: "gridPower",                   title: "Grid Power",               setting: "enableGridPower",             defaultEnabled: true,  slave: "plant",    reg: 30005, count: 2, type: "S32", scale: 1000, unit: "kW"],
    [name: "plantPower",                  title: "Plant Power",              setting: "enablePlantPower",            defaultEnabled: true,  slave: "plant",    reg: 30031, count: 2, type: "S32", scale: 1000, unit: "kW"],
    [name: "solarPower",                  title: "Solar Power",              setting: "enableSolarPower",            defaultEnabled: true,  slave: "plant",    reg: 30035, count: 2, type: "S32", scale: 1000, unit: "kW"],
    [name: "batteryPower",                title: "Battery Power",            setting: "enableBatteryPower",          defaultEnabled: true,  slave: "plant",    reg: 30037, count: 2, type: "S32", scale: 1000, unit: "kW"],
    [name: "thirdPartyPvPower",           title: "AC-Coupled PV Power",      setting: "enableThirdPartyPvPower",     defaultEnabled: true,  slave: "plant",    reg: 30194, count: 2, type: "S32", scale: 1000, unit: "kW"],
    [name: "totalLoadPower",              title: "Total Load Power",         setting: "enableTotalLoadPower",        defaultEnabled: true,  slave: "plant",    reg: 30284, count: 2, type: "S32", scale: 1000, unit: "kW"],
    [name: "batterySOH",                  title: "Battery Health (SOH)",     setting: "enableBatterySOH",            defaultEnabled: false, slave: "inverter", reg: 30602, count: 1, type: "U16", scale: 10,   unit: "%"],
    [name: "batteryTemperature",          title: "Battery Temperature",      setting: "enableBatteryTemp",           defaultEnabled: false, slave: "inverter", reg: 30603, count: 1, type: "S16", scale: 10,   unit: "C"],
    [name: "availableChargeEnergy",       title: "Available Charge Energy",  setting: "enableChargeEnergy",          defaultEnabled: false, slave: "inverter", reg: 30595, count: 2, type: "U32", scale: 100,  unit: "kWh"],
    [name: "availableDischargeEnergy",    title: "Available Discharge Energy", setting: "enableDischargeEnergy",     defaultEnabled: false, slave: "inverter", reg: 30597, count: 2, type: "U32", scale: 100,  unit: "kWh"],
    [name: "dailyImportEnergy",           title: "Grid Import Today",        setting: "enableDailyImport",           defaultEnabled: false, slave: "inverter", reg: 30560, count: 2, type: "U32", scale: 100,  unit: "kWh"],
    [name: "dailyExportEnergy",           title: "Grid Export Today",        setting: "enableDailyExport",           defaultEnabled: false, slave: "inverter", reg: 30554, count: 2, type: "U32", scale: 100,  unit: "kWh"],
    [name: "emsMode",                     title: "EMS Mode",                 setting: "enableEMSMode",               defaultEnabled: false, slave: "plant",    reg: 30003, count: 1, type: "U16", scale: 1,    unit: ""],
    [name: "gridSensorStatus",            title: "Grid Meter Status",        setting: "enableGridSensorStatus",      defaultEnabled: false, slave: "plant",    reg: 30004, count: 1, type: "U16", scale: 1,    unit: ""],
    [name: "onOffGridStatus",             title: "Grid Connection Status",   setting: "enableOnOffGridStatus",       defaultEnabled: false, slave: "plant",    reg: 30009, count: 1, type: "U16", scale: 1,    unit: ""],
    [name: "plantRunningState",           title: "Plant Operating State",    setting: "enablePlantRunningState",     defaultEnabled: false, slave: "plant",    reg: 30051, count: 1, type: "U16", scale: 1,    unit: ""],
    [name: "batteryDailyChargeEnergy",    title: "Battery Charge Today",     setting: "enableBatteryDailyCharge",    defaultEnabled: false, slave: "inverter", reg: 30566, count: 2, type: "U32", scale: 100,  unit: "kWh"],
    [name: "batteryDailyDischargeEnergy", title: "Battery Discharge Today",  setting: "enableBatteryDailyDischarge", defaultEnabled: false, slave: "inverter", reg: 30572, count: 2, type: "U32", scale: 100,  unit: "kWh"],
    [name: "gridFrequency",              title: "Grid Frequency",            setting: "enableGridFrequency",         defaultEnabled: true,  slave: "inverter", reg: 31002, count: 1, type: "U16", scale: 100,  unit: "Hz"],
    [name: "phaseAVoltage",              title: "Phase A Voltage",           setting: "enablePhaseAVoltage",         defaultEnabled: true,  slave: "inverter", reg: 31011, count: 2, type: "U32", scale: 100,  unit: "V"],
    [name: "phaseACurrent",              title: "Phase A Current",           setting: "enablePhaseACurrent",         defaultEnabled: true,  slave: "inverter", reg: 31017, count: 2, type: "S32", scale: 100,  unit: "A"],
    [name: "powerFactor",                title: "Power Factor",              setting: "enablePowerFactor",           defaultEnabled: true,  slave: "inverter", reg: 31023, count: 1, type: "S16", scale: 1000, unit: ""]
]

metadata {
    definition(name: "Sigenergy Plant Driver", namespace: "sigenergy", author: "mclass") {
        capability "Initialize"
        capability "Refresh"

        attribute "batterySOC", "number"
        attribute "gridPower", "number"
        attribute "plantPower", "number"
        attribute "solarPower", "number"
        attribute "batteryPower", "number"
        attribute "thirdPartyPvPower", "number"
        attribute "totalLoadPower", "number"
        attribute "batterySOH", "number"
        attribute "batteryTemperature", "number"
        attribute "availableChargeEnergy", "number"
        attribute "availableDischargeEnergy", "number"
        attribute "dailyImportEnergy", "number"
        attribute "dailyExportEnergy", "number"
        attribute "emsMode", "number"
        attribute "gridSensorStatus", "number"
        attribute "onOffGridStatus", "number"
        attribute "plantRunningState", "number"
        attribute "emsModeText", "string"
        attribute "gridSensorStatusText", "string"
        attribute "onOffGridStatusText", "string"
        attribute "plantRunningStateText", "string"
        attribute "batteryDailyChargeEnergy", "number"
        attribute "batteryDailyDischargeEnergy", "number"
        attribute "gridFrequency", "number"
        attribute "phaseAVoltage", "number"
        attribute "phaseACurrent", "number"
        attribute "powerFactor", "number"

        attribute "successfulPolls", "number"
        attribute "failedPolls", "number"
        attribute "lastError", "string"
        attribute "driverVersion", "string"
        attribute "lastPollTime", "string"
        attribute "lastResponseTime", "string"
    }
}

preferences {
    input "ipAddress", "string", title: "IP Address", required: true
    input "port", "number", title: "Port", defaultValue: 502
    input "slaveId", "number", title: "Inverter Slave ID", defaultValue: 1
    input "debugLogging", "bool", title: "Debug Logging", defaultValue: false
    input "pollInterval", "enum", title: "Poll Interval (minutes)", options: ["1", "5", "10", "15", "30"], defaultValue: "1"

    input name: "plantHdr", type: "paragraph", title: "Plant Measurements"
    input name: "measurementNote", type: "paragraph", title: "After changing measurement selections, save preferences and then refresh the device."
    REGISTER_DEFINITIONS.each { Map register ->
        input register.setting, "bool", title: register.title, defaultValue: register.defaultEnabled
    }
}

def installed() {
    state.successfulPolls = 0
    state.failedPolls = 0
    initialize()
}

def updated() {
    clearDisabledMeasurementAttributes()
    initialize()
}

def initialize() {
    unschedule()
    sendEvent(name: "driverVersion", value: DRIVER_VERSION)
    connectSocket()
    schedulePolling()
}

def refresh() {
    pollNow()
}

def connectSocket() {
    try {
        interfaces.rawSocket.close()
    } catch (e) {
        if (settings.debugLogging) log.debug "Socket close before connect ignored: ${e.message}"
    }

    try {
        interfaces.rawSocket.connect(settings.ipAddress, (settings.port ?: 502).toInteger(), byteInterface: true)
    } catch (e) {
        recordPollFailure("Socket connect failed: ${e.message}")
    }
}

def pollNow() {
    sendEvent(name: "lastPollTime", value: timestamp())
    state.queue = buildPollQueue()
    readNext()
}

def readNext() {
    if (!state.queue || state.queue.size() == 0) return

    Map item = state.queue.remove(0)
    state.currentItem = item

    String frame = String.format("000100000006%02X04%04X%04X", item.slave as Integer, item.reg as Integer, item.count as Integer)
    if (settings.debugLogging) log.info "TX ${item.name} slave=${item.slave} frame=${frame}"

    try {
        interfaces.rawSocket.sendMessage(frame)
    } catch (e) {
        recordPollFailure("Socket send failed: ${e.message}")
        connectSocket()
        readNext()
    }
}

def parse(message) {
    sendEvent(name: "lastResponseTime", value: timestamp())

    String hex = normalizeMessage(message)
    if (settings.debugLogging) log.info "RX=${hex}"

    Map item = state.currentItem
    if (!item) return

    if (isModbusException(hex)) {
        recordPollFailure("Modbus exception ${hex[-2..-1]}")
        readNext()
        return
    }

    Number value = decodeValue(hex, item)
    sendNumericEvent(item, value)
    recordPollSuccess()
    readNext()
}

private void schedulePolling() {
    switch ((settings.pollInterval ?: "1").toString()) {
        case "5":
            runEvery5Minutes("pollNow")
            break
        case "10":
            runEvery10Minutes("pollNow")
            break
        case "15":
            runEvery15Minutes("pollNow")
            break
        case "30":
            runEvery30Minutes("pollNow")
            break
        default:
            runEvery1Minute("pollNow")
            break
    }
}

private List<Map> buildPollQueue() {
    List<Map> queue = []
    REGISTER_DEFINITIONS.each { Map register ->
        if (isRegisterEnabled(register)) {
            Map item = new LinkedHashMap(register)
            item.slave = resolveSlaveId(register)
            queue << item
        }
    }
    return queue
}

private Boolean isRegisterEnabled(Map register) {
    def settingValue = settings[register.setting]
    if (settingValue == null) return register.defaultEnabled as Boolean
    return isPreferenceChecked(settingValue)
}

private Integer resolveSlaveId(Map register) {
    return register.slave == "inverter" ? (settings.slaveId ?: 1).toInteger() : PLANT_SLAVE_ID
}

private String normalizeMessage(message) {
    return (message instanceof byte[]) ? message.encodeHex().toString().toUpperCase() : message.toString().toUpperCase()
}

private Boolean isModbusException(String hex) {
    return hex?.length() >= 16 && hex.substring(14, 16) == "84"
}

private Number decodeValue(String hex, Map item) {
    switch (item.type) {
        case "U16":
            return Integer.parseInt(hex[-4..-1], 16) / item.scale
        case "S16":
            int raw16 = Integer.parseInt(hex[-4..-1], 16)
            if (raw16 > 32767) raw16 -= 65536
            return raw16 / item.scale
        case "U32":
            long rawU32 = Long.parseLong(hex[-8..-1], 16)
            return rawU32 / item.scale
        case "S32":
        default:
            long rawS32 = Long.parseLong(hex[-8..-1], 16)
            if (rawS32 > 2147483647L) rawS32 -= 4294967296L
            return rawS32 / item.scale
    }
}

private void sendNumericEvent(Map item, Number value) {
    if (item.unit) {
        sendEvent(name: item.name, value: value, unit: item.unit)
    } else {
        sendEvent(name: item.name, value: value)
    }
    sendStatusTextEvent(item.name, value)
}

private void sendStatusTextEvent(String attributeName, Number value) {
    String textAttributeName = STATUS_TEXT_ATTRIBUTES[attributeName]
    if (!textAttributeName) return

    Integer lookupValue = value.toInteger()
    String textValue = STATUS_TEXT_LOOKUPS[attributeName]?.get(lookupValue) ?: "Unknown (${lookupValue})"
    sendEvent(name: textAttributeName, value: textValue)
}

private void recordPollSuccess() {
    state.successfulPolls = (state.successfulPolls ?: 0) + 1
    sendEvent(name: "successfulPolls", value: state.successfulPolls)
}

private void recordPollFailure(String message) {
    state.failedPolls = (state.failedPolls ?: 0) + 1
    sendEvent(name: "failedPolls", value: state.failedPolls)
    sendEvent(name: "lastError", value: message)
}

private void clearDisabledMeasurementAttributes() {
    clearMeasurementAttributes { Map register -> !isRegisterEnabled(register) }
}

private void clearMeasurementAttributes(Closure<Boolean> shouldClearRegister) {
    REGISTER_DEFINITIONS.each { Map register ->
        if (shouldClearRegister(register)) {
            deleteCurrentStateSafely(register.name)
            String textAttributeName = STATUS_TEXT_ATTRIBUTES[register.name]
            if (textAttributeName) deleteCurrentStateSafely(textAttributeName)
        }
    }
}

private void deleteCurrentStateSafely(String attributeName) {
    try {
        device.deleteCurrentState(attributeName)
    } catch (e) {
        if (settings.debugLogging) log.debug "Unable to delete current state '${attributeName}': ${e.message}"
    }
}

private Boolean isPreferenceChecked(value) {
    if (value == null) return false
    if (value instanceof Boolean) return value
    return value.toString().toBoolean()
}

private String timestamp() {
    return new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
}
