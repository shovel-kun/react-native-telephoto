///
/// JHybridTelephotoStateUpdater.cpp
/// This file was generated by nitrogen. DO NOT MODIFY THIS FILE.
/// https://github.com/mrousavy/nitro
/// Copyright © 2025 Marc Rousavy @ Margelo
///

#include "JHybridTelephotoStateUpdater.hpp"
#include "views/HybridTelephotoComponent.hpp"
#include <NitroModules/NitroDefines.hpp>
#include <NitroModules/JNISharedPtr.hpp>

namespace margelo::nitro::telephoto::views {

using namespace facebook;
using ConcreteStateData = react::ConcreteState<HybridTelephotoState>;

void JHybridTelephotoStateUpdater::updateViewProps(jni::alias_ref<jni::JClass> /* class */,
                                           jni::alias_ref<JHybridTelephotoSpec::javaobject> javaView,
                                           jni::alias_ref<JStateWrapper::javaobject> stateWrapperInterface) {
  JHybridTelephotoSpec* view = javaView->cthis();
  
  // Get concrete StateWrapperImpl from passed StateWrapper interface object
  jobject rawStateWrapper = stateWrapperInterface.get();
  if (!stateWrapperInterface->isInstanceOf(react::StateWrapperImpl::javaClassStatic())) {
      throw std::runtime_error("StateWrapper is not a StateWrapperImpl");
  }
  auto stateWrapper = jni::alias_ref<react::StateWrapperImpl::javaobject>{
            static_cast<react::StateWrapperImpl::javaobject>(rawStateWrapper)};

  std::shared_ptr<const react::State> state = stateWrapper->cthis()->getState();
  auto concreteState = std::dynamic_pointer_cast<const ConcreteStateData>(state);
  const HybridTelephotoState& data = concreteState->getData();
  const std::optional<HybridTelephotoProps>& maybeProps = data.getProps();
  if (!maybeProps.has_value()) {
    // Props aren't set yet!
    throw std::runtime_error("HybridTelephotoState's data doesn't contain any props!");
  }
  const HybridTelephotoProps& props = maybeProps.value();
  if (props.source.isDirty) {
    view->setSource(props.source.value);
    // TODO: Set isDirty = false
  }
  if (props.contentDescription.isDirty) {
    view->setContentDescription(props.contentDescription.value);
    // TODO: Set isDirty = false
  }
  if (props.alignment.isDirty) {
    view->setAlignment(props.alignment.value);
    // TODO: Set isDirty = false
  }
  if (props.contentScale.isDirty) {
    view->setContentScale(props.contentScale.value);
    // TODO: Set isDirty = false
  }
  if (props.minZoomFactor.isDirty) {
    view->setMinZoomFactor(props.minZoomFactor.value);
    // TODO: Set isDirty = false
  }
  if (props.maxZoomFactor.isDirty) {
    view->setMaxZoomFactor(props.maxZoomFactor.value);
    // TODO: Set isDirty = false
  }
  if (props.onPress.isDirty) {
    view->setOnPress(props.onPress.value);
    // TODO: Set isDirty = false
  }
  if (props.onLongPress.isDirty) {
    view->setOnLongPress(props.onLongPress.value);
    // TODO: Set isDirty = false
  }
  if (props.onZoomFractionChanged.isDirty) {
    view->setOnZoomFractionChanged(props.onZoomFractionChanged.value);
    // TODO: Set isDirty = false
  }

  // Update hybridRef if it changed
  if (props.hybridRef.isDirty) {
    // hybridRef changed - call it with new this
    const auto& maybeFunc = props.hybridRef.value;
    if (maybeFunc.has_value()) {
      auto shared = JNISharedPtr::make_shared_from_jni<JHybridTelephotoSpec>(jni::make_global(javaView));
      maybeFunc.value()(shared);
    }
    // TODO: Set isDirty = false
  }
}

} // namespace margelo::nitro::telephoto::views
