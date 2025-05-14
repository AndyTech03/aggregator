import { likesArray } from "./faker/news"

export default async function fetchLikes(newsId) {
	return likesArray.filter(i => i.newsId == newsId)
}