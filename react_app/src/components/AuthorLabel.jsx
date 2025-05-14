import useArray from "../hooks/useArray";
import AddOrDelButton from "./AddOrDelButton";

function AuthorLabel({ author, enableFilter=false, ...props }) {
	const authorsFilter = useArray({
			cookiesName: 'authorsFilter',
			defaultValue: [],
			useStorage: true,
			global: true,
			disabled: enableFilter != true,
		})
	return ( 
		<div {...props}>
			{author != null && author != '' ?
				<b>
					Автор: {author}
					{enableFilter && authorsFilter != null &&
						<AddOrDelButton item={author} itemsArray={authorsFilter}/>
					}
				</b> :
				<>Аноним</>
			}
		</div>
	);
}

export default AuthorLabel;