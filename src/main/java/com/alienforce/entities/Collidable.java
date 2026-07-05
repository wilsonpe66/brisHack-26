package com.alienforce.entities;

public interface Collidable {

    /// Called when this object collides the given object.
    ///
    /// @param collidable the object being collided with.
    void collide(Collidable collidable);
}

