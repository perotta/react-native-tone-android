# react-native-tone-android

Android only library. Simply plays a one time Tone with specific frequency and duration.

## Getting started

`$ yarn add react-native-tone-android`

## Usage

```javascript
import Tone from "react-native-tone-android";

function somefunction() {
  const frequency = 4000; //Hz
  const duration = 50; // ms

  Tone.play(frequency, duration);
}
```
