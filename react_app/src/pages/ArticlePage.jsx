import { useEffect, useState } from "react"
import fetchArticle from "../api/fetchArticle"
import NotFoundPage from "./NotFound"
import { Link, useParams } from "react-router-dom"
import AddOrDelButton from "../components/AddOrDelButton"
import useArray from "../hooks/useArray"
import NewsFeed from "../components/NewsFeed"

function ArticlePage({ ...props }) {
	const similarCount = 10;
	const params = useParams()
	const newsId = params?.id
	console.log(params);
	const [item, setItem] = useState(null)
	const [state, setState] = useState('loading')

	const categoriesFilter = useArray({
		cookiesName: 'categoriesFilter',
		defaultValue: [],
		useStorage: true,
		global: true,
	})
	const authorsFilter = useArray({
		cookiesName: 'authorsFilter',
		defaultValue: [],
		useStorage: true,
		global: true,
	})
	const rssFeedsFilter = useArray({
		cookiesName: 'rssFeedsFilter',
		defaultValue: [],
		useStorage: true,
		global: true,
	})
	
	useEffect(() => {
		fetchArticle(newsId)
		.then(found => {
			if (found == null) {
				setState('notFound')
				return
			}
			setItem(found)
			setState('found')
		})
	}, [params.id])

	if (state == 'notFound') {
		return <NotFoundPage />
	}
	if (state != 'found') {
		return (
			<div>
				Loading...
			</div>
		)
	}

	const date = new Date(item.date)
	const originUri = item.uri
	const author = item.author
	const feedUrl = item.feedUrl
	return ( 
		<div {...props}>
			<hr />
			<span>
				{item.title}
				[
					<Link to={originUri} onClick={() => alert('click')}>открыть</Link>
				]
			</span>
			<div>
				{item.categories.map((category, key) => 
					<span key={key}>
						<b>#{category}</b>
						{categoriesFilter != null &&
							<AddOrDelButton item={category} itemsArray={categoriesFilter}/>
						}
					</span>
				)}
			</div>
			<p dangerouslySetInnerHTML={{__html: item.description}}/>
			<span>
				<b>@{author}</b>
				{authorsFilter != null &&
					<AddOrDelButton item={author} itemsArray={authorsFilter}/>
				}
				<br />
				{date.toLocaleString()}
				<br />
				{feedUrl}
				{rssFeedsFilter != null &&
					<AddOrDelButton item={feedUrl} itemsArray={rssFeedsFilter}/>
				}
			</span>
			<hr />
			<br />
			<NewsFeed title="Похожие Новости" pageSize={similarCount} similar={newsId} />
		</div>
	 );
}

export default ArticlePage;