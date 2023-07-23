<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Generate Sales Report</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/themes/base/jquery-ui.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
</head>
<body>
    <h1>Generate Sales Report</h1>
    <form action="GenerateReportServlet" method="post">
        <label>Choose report type:</label>
        <select name="reportType" id="reportTypeSelector">
            <option value="byDate">By Date (YYYY-MM-DD)</option>
            <option value="byMonth">By Month (YYYYMM)</option>
            <option value="byPeriod">By Period (YYYY-MM-DD to YYYY-MM-DD)</option>
        </select>
        <br>
        <div id="datePickerContainer" style="display: none;">
            <label>Choose a date:</label>
            <input type="text" name="selectedDate" id="datePicker">
            <br>
        </div>
        <div id="monthPickerContainer" style="display: none;">
            <label>Choose a month:</label>
            <input type="text" name="selectedMonth" id="monthPicker">
            <br>
        </div>
        <div id="periodPickerContainer" style="display: none;">
            <label>Enter start date:</label>
            <input type="text" name="startDate" id="startDatePicker">
            <br>
            <label>Enter end date:</label>
            <input type="text" name="endDate" id="endDatePicker">
            <br>
        </div>
        <input type="submit" value="Generate Report">
    </form>

    <script>
        $(document).ready(function() {
            $("#reportTypeSelector").change(function() {
                var selectedOption = $(this).val();
                $("#datePickerContainer").toggle(selectedOption === "byDate");
                $("#monthPickerContainer").toggle(selectedOption === "byMonth");
                $("#periodPickerContainer").toggle(selectedOption === "byPeriod");
            });

            $("#datePicker").datepicker({
                dateFormat: 'yy-mm-dd',
                changeMonth: true,
                changeYear: true
            });

            $("#monthPicker").datepicker({
                dateFormat: 'yymm',
                changeMonth: true,
                changeYear: true,
                showButtonPanel: true,
                onClose: function(dateText, inst) {
                    var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                    var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                    $(this).val(year + month);
                }
            });

            $("#startDatePicker, #endDatePicker").datepicker({
                dateFormat: 'yy-mm-dd',
                changeMonth: true,
                changeYear: true
            });
        });
    </script>
</body>
</html>
