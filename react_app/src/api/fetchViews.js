import { viewsArray } from "./faker/news"

export default async function fetchViews(newsId) {
	return viewsArray.filter(i => i.newsId == newsId)
}