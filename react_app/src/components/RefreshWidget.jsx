import React from "react";


function RefreshWidget({ refresh, ...props }) {
	return ( 
		<div {...props} style={{position: 'fixed', bottom: '1em', left: '1em'}}>
			<button onClick={() => refresh()}>Refresh</button>
		</div>
	);
}

export default RefreshWidget;