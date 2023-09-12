package me.product.api;

import com.google.gson.JsonElement;
import lombok.Data;

@Data
public class ProductResponse
{
    /**
     * Flag set if the request has succeeded.
     */
    private final boolean success;

    /**
     * A message field, will be set if the operation has failed.
     */
    private final String message;

    /**
     * Any output data generated by the request.
     */
    private final JsonElement data;
}
