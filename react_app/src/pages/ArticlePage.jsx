import { useEffect, useState } from "react"
import fetchArticle from "../api/fetchArticle"
import NotFoundPage from "./NotFound"
import { Link, useParams } from "react-router-dom"
import AddOrDelButton from "../components/AddOrDelButton"

function ArticlePage({...props}) {
	const params = useParams()
	console.log(params);
	
	const [item, setItem] = useState(null)
	const [state, setState] = useState('loading')
	const filter = null
	useEffect(() => {
		fetchArticle(params.id)
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
						{filter &&
							<AddOrDelButton item={category} itemsArray={filter.categories}/>
						}
						
					</span>
				)}
			</div>
			<p dangerouslySetInnerHTML={{__html: item.description}}/>
			<span>
				<b>@{author}</b>
				{filter &&
					<AddOrDelButton item={author} itemsArray={filter.authors}/>
				}
				<br />
				{date.toLocaleString()}
				<br />
				{feedUrl}
				{filter &&
					<AddOrDelButton item={feedUrl} itemsArray={filter.rssFeeds}/>
				}
			</span>
		</div>
	 );
}

export default ArticlePage;