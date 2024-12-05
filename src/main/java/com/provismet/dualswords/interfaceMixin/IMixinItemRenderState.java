package com.provismet.dualswords.interfaceMixin;

public interface IMixinItemRenderState {
    void dual_Swords$setReverseRender (boolean value);
    void dual_Swords$setFlippedSpear (boolean value);
    void dual_Swords$setIsActive (boolean value);

    boolean dual_Swords$shouldReverseRender ();
    boolean dual_Swords$shouldFlipSpear ();
    boolean dual_Swords$isActive ();
}
