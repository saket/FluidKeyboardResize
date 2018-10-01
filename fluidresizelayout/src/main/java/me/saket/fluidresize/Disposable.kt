package me.saket.fluidresize

class Disposable(private val onDispose: () -> Unit) {

  fun dispose() {
    onDispose()
  }
}
