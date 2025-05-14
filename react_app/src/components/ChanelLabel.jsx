import useArray from "../hooks/useArray";
import AddOrDelButton from "./AddOrDelButton";

function ChanelLabel({ rssFeedTitle, enableFilter=false, ...props }) {
	const rssFeedsFilter = useArray({
			cookiesName: 'rssFeedsFilter',
			defaultValue: [],
			useStorage: true,
			global: true,
			disabled: enableFilter != true,
		})
	return ( 
		<div {...props}>
			{rssFeedTitle != null ?
				<b>
					Канал: {rssFeedTitle}
					{enableFilter && rssFeedsFilter != null &&
						<AddOrDelButton item={rssFeedTitle} itemsArray={rssFeedsFilter}/>
					}
				</b> :
				<>Нет данных</>
			}
		</div>
	);
}

export default ChanelLabel;