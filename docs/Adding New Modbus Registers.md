# Adding New Modbus Registers

This driver has been designed so that new Sigenergy Modbus registers can normally be added without modifying the polling engine or communications code.

In most cases, only the register definition table and Hubitat metadata require updating.

---

## Step 1 – Confirm the Register

Using the latest Sigenergy Modbus Protocol document, identify:

* Register number
* Register name
* Register type (U16, S16, U32 or S32)
* Scale (Gain)
* Units
* Whether the register is read from:

  * Plant (Slave ID 247)
  * Inverter (Configured Slave ID)

Example:

| Register | Name           | Type | Scale | Unit |
| -------- | -------------- | ---- | ----: | ---- |
| 31002    | Grid Frequency | U16  |   100 | Hz   |

---

## Step 2 – Add the Register Definition

Locate:

```groovy
REGISTER_DEFINITIONS
```

Add a new entry.

Example:

```groovy
[
    name: "gridFrequency",
    title: "Grid Frequency",
    setting: "enableGridFrequency",
    defaultEnabled: true,
    slave: "inverter",
    reg: 31002,
    count: 1,
    type: "U16",
    scale: 100,
    unit: "Hz"
]
```

Most new measurements only require this single addition.

---

## Step 3 – Add the Attribute

In the `metadata` section add:

```groovy
attribute "gridFrequency", "number"
```

For text status attributes use:

```groovy
attribute "emsModeText", "string"
```

---

## Step 4 – Preferences

No code changes are normally required.

Preferences are generated automatically from `REGISTER_DEFINITIONS`.

If `defaultEnabled` is:

```groovy
true
```

the measurement is enabled by default.

If:

```groovy
false
```

the user may enable it manually.

---

## Step 5 – Status Registers

If the register represents an enumerated status rather than a numeric value:

1. Keep the numeric attribute.
2. Add a matching text attribute.
3. Add the lookup table.
4. Add the text attribute name to `STATUS_TEXT_ATTRIBUTES`.

Example:

```groovy
emsMode = 2
emsModeText = TOU
```

This preserves Rule Machine compatibility while providing readable status values.

---

## Step 6 – Update Version

Increment:

```groovy
DRIVER_VERSION
```

Update the Release History.

Update `CHANGELOG.md`.

---

## Step 7 – Test

Recommended testing:

* Driver compiles successfully.
* Preferences page displays the new measurement.
* Polling completes normally.
* Attribute updates correctly.
* Units display correctly.
* Existing measurements remain unchanged.

---

## Design Philosophy

The driver is intentionally table-driven.

Whenever possible:

* Do not modify the polling engine.
* Do not modify socket communications.
* Do not modify Modbus decoding.
* Add new measurements by extending the register definition table.

This approach minimises risk and keeps future maintenance straightforward.
