import { getHostComponent, type HybridRef } from 'react-native-nitro-modules'
import TelephotoConfig from '../nitrogen/generated/shared/json/TelephotoConfig.json'
import type {
  TelephotoProps,
  TelephotoMethods,
} from './views/telephoto.nitro'


export const Telephoto = getHostComponent<TelephotoProps, TelephotoMethods>(
  'Telephoto',
  () => TelephotoConfig
)

export type TelephotoRef = HybridRef<TelephotoProps, TelephotoMethods>
