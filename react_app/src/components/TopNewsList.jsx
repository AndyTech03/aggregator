import { useEffect, useState } from "react";
import { getTopNews } from "../api/trendsController";
import {useLoad, LoadStatus} from "../hooks/useLoad";
import { Link } from "react-router-dom";
import routes from "../routes";
import useValue from "../hooks/useValue";

function TopNewsList({ ...props }) {
	const [offset, setOffset] = useState(0)
	const [limit, setLimit] = useState(10)
	const [date, setDate] = useState(null)
	const [news, setNews, saveNews] = useValue({
		cookiesName: 'topNews',
		defaultValue: [],
		useStorage: true,
		global: true,
	})
	const [loader, loading, cancel, reload] = useLoad(getTopNews, [offset, limit])

	const changeDay = async (newDate) => {
		let today = new Date(newDate)
		today.setHours(0, 0, 0, 0)
		if (today == newDate)
			return
		if (date != null)
			setNews(null)
		setDate(today)
	}

	useEffect(() => {
		if (date == null)
			return
		loader(offset, limit, date)
		.then((result) => {
			if (result == null)
				setNews([])
			console.log(result);
			
			setNews(result)
			saveNews(result)
		})
	}, [offset, limit, date])

	useEffect(() => {
		changeDay(new Date())
	}, [])
	
	return (
		<div {...props}>
			{loading == LoadStatus.LOADING && (news == null || news.length == 0) ?
				<>Loading... <br /></> :
			loading == LoadStatus.ERROR ?
				<>Error! <br /></> :
				<>
					{new Date(date).toDateString()}
					{news.map((item, idx) => 
						<Link to={routes.ARTICLE_PAGE.replace(':id', item.id)} key={item.id}>
							<p>{idx+1}. {item.title}</p>
						</Link>
					)}
				</>
			}
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

export default TopNewsList;