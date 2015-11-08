package com.example.android.moviestrends;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by praveena on 10/11/2015.
 */
//BlurTransformation is used for reviews screen to blur the background image
public class BlurTransformation implements com.squareup.picasso.Transformation {
    private final float radius;
    RenderScript rs;

    // radius is corner radii in dp
    public BlurTransformation(final float radius) {
        this.radius = radius;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public Bitmap transform(final Bitmap source) {
        // do the transformation and return it here
        Bitmap blurredBitmap = Bitmap.createBitmap(source);
        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(rs, source, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(rs, input.getType());
        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);


        // Set the blur radius
        script.setRadius(radius);


        // Start the ScriptIntrinisicBlur
        script.forEach(output);


        // Copy the output to the blurred bitmap
        output.copyTo(blurredBitmap);
        return blurredBitmap;
    }

    @Override
    public String key() {
        return "blur" + Float.toString(radius);
    }
}
