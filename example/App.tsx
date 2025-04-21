import React from 'react';
import {View, StyleSheet} from 'react-native';
import {Telephoto} from 'react-native-telephoto';

function App(): React.JSX.Element {
  return (
    <View style={styles.container}>
      <Telephoto
        source={'https://images.unsplash.com/photo-1506744038136-46273834b3fb'}
        contentScale="fit"
        style={styles.view}
        minZoomFactor={1}
        maxZoomFactor={2}
        onPress={{
          f: offset => {
            console.log('onPress', offset);
          },
        }}
        onLongPress={{
          f: offset => {
            console.log('onLongPress', offset);
          },
        }}
      />
    </View>
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
});

export default App;
