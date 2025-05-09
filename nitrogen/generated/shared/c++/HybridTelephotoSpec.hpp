///
/// HybridTelephotoSpec.hpp
/// This file was generated by nitrogen. DO NOT MODIFY THIS FILE.
/// https://github.com/mrousavy/nitro
/// Copyright © 2025 Marc Rousavy @ Margelo
///

#pragma once

#if __has_include(<NitroModules/HybridObject.hpp>)
#include <NitroModules/HybridObject.hpp>
#else
#error NitroModules cannot be found! Are you sure you installed NitroModules properly?
#endif

// Forward declaration of `Alignment` to properly resolve imports.
namespace margelo::nitro::telephoto { enum class Alignment; }
// Forward declaration of `ContentScale` to properly resolve imports.
namespace margelo::nitro::telephoto { enum class ContentScale; }
// Forward declaration of `Offset` to properly resolve imports.
namespace margelo::nitro::telephoto { struct Offset; }

#include <string>
#include <optional>
#include "Alignment.hpp"
#include "ContentScale.hpp"
#include <functional>
#include "Offset.hpp"
#include <NitroModules/Promise.hpp>

namespace margelo::nitro::telephoto {

  using namespace margelo::nitro;

  /**
   * An abstract base class for `Telephoto`
   * Inherit this class to create instances of `HybridTelephotoSpec` in C++.
   * You must explicitly call `HybridObject`'s constructor yourself, because it is virtual.
   * @example
   * ```cpp
   * class HybridTelephoto: public HybridTelephotoSpec {
   * public:
   *   HybridTelephoto(...): HybridObject(TAG) { ... }
   *   // ...
   * };
   * ```
   */
  class HybridTelephotoSpec: public virtual HybridObject {
    public:
      // Constructor
      explicit HybridTelephotoSpec(): HybridObject(TAG) { }

      // Destructor
      ~HybridTelephotoSpec() override = default;

    public:
      // Properties
      virtual std::string getSource() = 0;
      virtual void setSource(const std::string& source) = 0;
      virtual std::optional<std::string> getContentDescription() = 0;
      virtual void setContentDescription(const std::optional<std::string>& contentDescription) = 0;
      virtual std::optional<Alignment> getAlignment() = 0;
      virtual void setAlignment(std::optional<Alignment> alignment) = 0;
      virtual std::optional<ContentScale> getContentScale() = 0;
      virtual void setContentScale(std::optional<ContentScale> contentScale) = 0;
      virtual std::optional<double> getMinZoomFactor() = 0;
      virtual void setMinZoomFactor(std::optional<double> minZoomFactor) = 0;
      virtual std::optional<double> getMaxZoomFactor() = 0;
      virtual void setMaxZoomFactor(std::optional<double> maxZoomFactor) = 0;
      virtual std::optional<std::function<void(const Offset& /* offset */)>> getOnPress() = 0;
      virtual void setOnPress(const std::optional<std::function<void(const Offset& /* offset */)>>& onPress) = 0;
      virtual std::optional<std::function<void(const Offset& /* offset */)>> getOnLongPress() = 0;
      virtual void setOnLongPress(const std::optional<std::function<void(const Offset& /* offset */)>>& onLongPress) = 0;
      virtual std::optional<std::function<void(std::optional<double> /* factor */)>> getOnZoomFractionChanged() = 0;
      virtual void setOnZoomFractionChanged(const std::optional<std::function<void(std::optional<double> /* factor */)>>& onZoomFractionChanged) = 0;

    public:
      // Methods
      virtual std::shared_ptr<Promise<void>> zoomTo(double factor, const Offset& centroid) = 0;
      virtual std::shared_ptr<Promise<void>> zoomBy(double factor, const Offset& centroid) = 0;
      virtual std::shared_ptr<Promise<void>> resetZoom() = 0;

    protected:
      // Hybrid Setup
      void loadHybridMethods() override;

    protected:
      // Tag for logging
      static constexpr auto TAG = "Telephoto";
  };

} // namespace margelo::nitro::telephoto
