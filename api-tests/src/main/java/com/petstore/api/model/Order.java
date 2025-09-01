package com.petstore.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.petstore.api.model.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Order{

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("petId")
	private Integer petId;

	@JsonProperty("quantity")
	private Integer quantity;

	@JsonProperty("shipDate")
	private String shipDate;

	@JsonProperty("status")
	private OrderStatus status;

	@JsonProperty("complete")
	private Boolean complete;
}