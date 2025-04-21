import React, {useRef, useState} from 'react';
import {View, StyleSheet, Button} from 'react-native';
import {Telephoto, TelephotoRef} from 'react-native-telephoto';
import 'react-native-gesture-handler';
import {
  Gesture,
  GestureDetector,
  GestureHandlerRootView,
} from 'react-native-gesture-handler';

function App(): React.JSX.Element {
  const telephotoRef = useRef<TelephotoRef>(null);
  const [zoomFraction, setZoomFraction] = useState<number | null>(null);

  const singleTap = Gesture.Tap()
    .maxDuration(250)
    .onStart(({absoluteX, absoluteY}) => {
      console.log('Single tap!', {absoluteX, absoluteY});
    });

  const doubleTap = Gesture.Tap()
    .maxDuration(250)
    .numberOfTaps(2)
    .onStart(({absoluteX, absoluteY}) => {
      console.log('Double tap!', {absoluteX, absoluteY});
      if (zoomFraction === null || zoomFraction === 0) {
        telephotoRef.current?.zoomTo(2, {x: absoluteX, y: absoluteY});
      } else {
        telephotoRef.current?.resetZoom();
      }
    });

  return (
    <GestureHandlerRootView>
      <View style={styles.container}>
        <GestureDetector gesture={Gesture.Exclusive(doubleTap, singleTap)}>
          <Telephoto
            hybridRef={{
              f: ref => {
                telephotoRef.current = ref;
              },
            }}
            source={
              'https://images.unsplash.com/photo-1506744038136-46273834b3fb'
            }
            contentScale="fit"
            style={styles.view}
            minZoomFactor={1}
            maxZoomFactor={2}
            onZoomFractionChanged={{
              f: e => {
                // console.log('onZoomFractionChanged', e);
                setZoomFraction(e);
              },
            }}
            // onPress={{
            //   f: offset => {
            //     console.log('onPress', offset);
            //   },
            // }}
            // onLongPress={{
            //   f: offset => {
            //     console.log('onLongPress', offset);
            //   },
            // }}
          />
        </GestureDetector>
      </View>
      <View style={styles.button}>
        <Button
          title="zoom"
          onPress={() => {
            console.log('zoom');
            telephotoRef.current?.zoomBy(2, {x: 0, y: 0});
          }}
        />
      </View>
    </GestureHandlerRootView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  view: {
    width: '100%',
    height: '100%',
    backgroundColor: 'black',
  },
  button: {
    height: 50,
    width: '100%',
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 50,
  },
});

export default App;
