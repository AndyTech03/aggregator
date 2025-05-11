import React from "react";


function RefreshWidget({ reset, goTop, goBottom, ...props }) {
	return ( 
		<div {...props} style={{position: 'fixed', bottom: '1em', left: '1em'}}>
			{goTop != null &&
				<>
					<button onClick={() => goTop()}>Scroll Top</button>
					<br />
				</>
			}
			{reset != null &&
				<>
					<button onClick={() => reset()}>Reset</button>
					<br />
				</>
			}
			{goBottom != null &&
				<>
					<button onClick={() => goBottom()}>Scroll Bottom</button>
					<br />
				</>
			}
		</div>
	);
}

export default RefreshWidget;