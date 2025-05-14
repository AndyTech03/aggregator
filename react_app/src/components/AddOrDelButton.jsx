import React from "react";


const AddOrDelButton = ({ item, itemsArray, ...props }) => {
	return (
		<span {...props}>
			{itemsArray?.contains(item) ?
				<button onClick={() => itemsArray.delItem(item)}>-</button> :
				<button onClick={() => itemsArray.addItem(item)}>+</button>
			}
		</span>
	)
}

export default AddOrDelButton;