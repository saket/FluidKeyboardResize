package me.saket.fluidresize;

interface KeyboardVisibilityChangedListener {

    void onKeyboardVisibilityChanged(KeyboardVisibilityChangedEvent visibilityChanged);

    class KeyboardVisibilityChangedEvent {
        private final boolean visible;
        private final int contentHeight;
        private final int contentHeightBeforeResize;

        KeyboardVisibilityChangedEvent(boolean visible, int contentHeight, int contentHeightBeforeResize) {
            this.visible = visible;
            this.contentHeight = contentHeight;
            this.contentHeightBeforeResize = contentHeightBeforeResize;
        }

        public boolean isVisible() {
            return visible;
        }

        public int getContentHeight() {
            return contentHeight;
        }

        public int getContentHeightBeforeResize() {
            return contentHeightBeforeResize;
        }
    }
}
