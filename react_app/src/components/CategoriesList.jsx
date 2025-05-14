import useArray from "../hooks/useArray";
import AddOrDelButton from "./AddOrDelButton";

function CategoriesList({ categories, enableFilter=false, ...props }) {
	const categoriesFilter = useArray({
		cookiesName: 'categoriesFilter',
		defaultValue: [],
		useStorage: true,
		global: true,
		disabled: enableFilter != true,
	})

	return ( 
		<div {...props}>
			{categories?.map((category, key) => 
				<span key={key}>
					"{category}"
					{enableFilter && categoriesFilter != null &&
						<AddOrDelButton item={category} itemsArray={categoriesFilter}/>
					}
					{key == categories.length -1 ? "" : ", "}
				</span>
			)}
		</div>
	);
}

export default CategoriesList;