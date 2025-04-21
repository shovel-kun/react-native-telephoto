import type {
  HybridView,
  HybridViewProps,
  HybridViewMethods,
} from 'react-native-nitro-modules'

export interface TelephotoProps extends HybridViewProps {
   isRed: boolean
}

export interface TelephotoMethods extends HybridViewMethods {}

export type Telephoto = HybridView<TelephotoProps, TelephotoMethods, { ios: 'swift', android: 'kotlin' }>