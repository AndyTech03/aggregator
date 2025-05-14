function DateLabel({ date, ...props }) {
	return ( 
		<div {...props}>
			{date != null ?
				<>{date.toLocaleString()}</> :
				<>Нет данных</>
			}
		</div>
	);
}

export default DateLabel;