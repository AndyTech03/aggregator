function LikesViewsLabel({ likesCount, viewsCount, ...props }) {
	return (
		<div {...props}>
			<>{likesCount} likes</>
			<br />
			<>{viewsCount} views</>
		</div>
	);
}

export default LikesViewsLabel;