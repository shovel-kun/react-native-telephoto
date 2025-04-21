import type {
  HybridView,
  HybridViewProps,
  HybridViewMethods,
} from 'react-native-nitro-modules'

interface Offset {
  x: number
  y: number
}

type Alignment =
  | 'top-start'
  | 'top-center'
  | 'top-end'
  | 'center-start'
  | 'center'
  | 'center-end'
  | 'bottom-start'
  | 'bottom-center'
  | 'bottom-end'

type ContentScale =
  | 'crop'
  | 'fit'
  | 'fill-width'
  | 'fill-height'
  | 'inside'
  | 'none'
  | 'fill-bounds'

export interface TelephotoProps extends HybridViewProps {
  /**
   * The source of the image
   */
  source: string
  /**
   * The content description of the image
   */
  contentDescription?: string
  /**
   * The alignment of the image
   *
   * @default 'center'
   */
  alignment?: Alignment
  /**
   * The content scale of the image
   *
   * @default 'fit'
   */
  contentScale?: ContentScale
  /**
   * The minimum zoom factor of the image
   *
   * @default 1
   */
  minZoomFactor?: number
  /**
   * The maximum zoom factor of the image
   *
   * @default 2
   */
  maxZoomFactor?: number
  /**
   * The onPress handler of the image
   *
   * NOTE:
   * Clicks are delayed until they're confirmed to not be double clicks
   * so make sure that onClick does not get called prematurely.
   */
  onPress?: (offset: Offset) => void
  /**
   * The onLongPress handler of the image
   */
  onLongPress?: (offset: Offset) => void
  /**
   * The onZoomFrom handler of the image
   *
   * @return A value between 0 and 1, where 0 indicates that the content is fully zoomed out,
   * 1 indicates that the content is fully zoomed in, and `null` indicates that an initial zoom
   * value hasn't been calculated yet and the content is hidden.
   * A `null` value could be safely treated the same as 0.
   */
  onZoomFractionChanged?: (factor: number | null) => void
}

export interface TelephotoMethods extends HybridViewMethods {
  zoomTo(factor: number, centroid: Offset): Promise<void>
  zoomBy(factor: number, centroid: Offset): Promise<void>
  resetZoom(): Promise<void>
}

export type Telephoto = HybridView<
  TelephotoProps,
  TelephotoMethods,
  { ios: 'swift'; android: 'kotlin' }
>
