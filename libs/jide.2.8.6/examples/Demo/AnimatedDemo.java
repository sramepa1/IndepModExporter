/*
 * @(#)AnimatedDemo.java 8/24/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 */

/**
 * This interface introduced so that if a demo uses animation, the demo runs only when the demo window is active.
 * Otherwise it would be using up CPU cycles unnecessarily.
 */
public interface AnimatedDemo extends Demo {
    void startAnimation();

    void stopAnimation();
}