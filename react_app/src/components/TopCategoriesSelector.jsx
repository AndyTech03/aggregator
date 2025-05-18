import { useEffect, useState } from "react"
import { getTopCategories } from "../api/trendsController"
import { LoadStatus, useLoad } from "../hooks/useLoad"
import useValue from "../hooks/useValue"

function TopCategoriesSelector({ categoriesFilter, ...props }) {
	const [offset, setOffset] = useState(0)
	const [limit, setLimit] = useState(10)
	const [date, setDate] = useState(null)

	const [categories, setCategories, saveCategories] = useValue({
		cookiesName: 'topCategories',
		defaultValue: [],
		useStorage: true,
		global: true,
	})

	const [loader, loading, cancel, reload] = useLoad(getTopCategories, [offset, limit])

	const changeDay = async (newDate) => {
		let today = new Date(newDate)
		today.setHours(0, 0, 0, 0)
		if (today == newDate)
			return
		if (date != null)
			setCategories(null)
		setDate(today)
	}

	useEffect(() => {
		if (date == null)
			return
		loader(offset, limit, date)
		.then((result) => {
			if (result == null)
				setCategories([])
			console.log(result);
			
			setCategories(result)
			saveCategories(result)
		})
	}, [offset, limit, date])

	useEffect(() => {
		changeDay(new Date())
	}, [])
	
	return (
		<div {...props}>
			{new Date(date).toDateString()} <br />
			{categoriesFilter?.items?.map((item, idx) => 
					<>
						{item}
						<input type='checkbox' 
							value={item} 
							key={item} 
							checked={true}
							onChange={(e) => categoriesFilter.delItem(e.target.value)}
						/>
						{"; "}
					</>
				)
			}
			<br />
			{loading == LoadStatus.LOADING && (categories == null || categories.length == 0) ?
				<>Loading... <br /></> :
			loading == LoadStatus.ERROR ?
				<>Error! <br /></> :
				<>
					{categories
					.filter(item => categoriesFilter.contains(item) == false)
					.map((item, idx) => 
						<>
							{item}
							<input type='checkbox' 
								value={item} 
								key={item} 
								checked={false} 
								onChange={(e) => categoriesFilter.addItem(e.target.value)}
							/>
							{"; "}
						</>
					)}
				</>
			}
			<br />
			<button onClick={() => {
				const day = new Date(date)
				day.setDate(day.getDate() - 1)
				changeDay(day)
			}}>Before</button>
			<button onClick={() => {
				const day = new Date(date)
				day.setDate(day.getDate() + 1)
				if (day > new Date())
					return
				changeDay(day)
			}}>After</button>
		</div>
	);
}

export default TopCategoriesSelector;