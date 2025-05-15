import { useEffect, useState } from "react";
import { getTopNews } from "../api/trendsController";
import {useLoad, LoadStatus} from "../hooks/useLoad";
import { Link } from "react-router-dom";
import routes from "../routes";

function TopNewsList({ ...props }) {
	const [offset, setOffset] = useState(0)
	const [limit, setLimit] = useState(10)
	const [date, setDate] = useState(new Date())
	const [news, setNews] = useState([])
	const [loader, loading, cancel, reload] = useLoad(getTopNews, [offset, limit])
	useEffect(() => {
		loader(offset, limit, date)
		.then((result) => {
			if (result == null)
				setNews([])
			console.log(result);
			
			setNews(result)
		})
	}, [offset, limit, date])
	return (
		<div {...props}>
			{loading == LoadStatus.LOADING ?
				<>Loading...</> :
			loading == LoadStatus.ERROR ?
				<>Error!</> :
			news.map((item, idx) => 
				<Link to={routes.ARTICLE_PAGE.replace(':id', item.id)} key={item.id}>
					<p>{idx+1}. {item.title}</p>
				</Link>
			)
			}
		</div>
	);
}

export default TopNewsList;