# Changelog

## 2.2.0 - 2026-06-28

### Added
- `emsModeText`
- `gridSensorStatusText`
- `onOffGridStatusText`
- `plantRunningStateText`

These string attributes are decoded from the existing numeric status registers using the Sigenergy Modbus Protocol V2.9 lookup tables whenever those registers are updated.

### Changed
- Driver version updated to 2.2.0.
- Existing numeric status attributes remain unchanged for Rule Machine compatibility.
- No polling engine, queue management, socket handling or register decoding logic changes.

## 2.1.0 - 2026-06-28

### Added
- Grid Frequency (31002)
- Phase A Voltage (31011)
- Phase A Current (31017)
- Power Factor (31023)

All four measurements are enabled by default and use the existing polling engine.

### Changed
- Driver version updated to 2.1.0.
- No communication or polling logic changes.
