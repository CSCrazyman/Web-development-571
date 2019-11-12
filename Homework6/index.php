<?php
$chosen = "";
if(isset($_GET['submit'])){
    $chosen = "table";
    $error = "";
    $keyword = urlencode($_GET['key']);
    $categoryID = $_GET['category'];
    $postalCode = "";
    $filterArr = array();
    if(isset($_GET['location'])){
        if($_GET['location'] == "zip"){
            $postalCode = $_GET['zipcode'];
            if(!preg_match("/^[0-9]{5}(-[0-9]{4})?$/", $postalCode)){
                $error = "true";
            }
        }
        else{ 
            $postalCode = $_GET['nowzip']; 
        }
    }
    if(isset($_GET['distance'])){
        if($_GET['distance'] != ""){ 
            $filterArr['MaxDistance'] = $_GET['distance'];
        }
        else{
            $filterArr['MaxDistance'] = "10";
        }
    }
    if(isset($_GET['freeshipping'])){
        $filterArr['FreeShippingOnly'] = "true";
    }
    if(isset($_GET['localpickup'])){
        $filterArr['LocalPickupOnly'] = "true";
    }
    $filterArr['HideDuplicateItems'] = "true";
    $condition = isset($_GET['new']) || isset($_GET['used']) || isset($_GET['unsp']);
    if($condition){
        $filterArr['Condition'] = array();
        if(isset($_GET['new'])){
            array_push($filterArr['Condition'], "New"); 
        }
        if(isset($_GET['used'])){
            array_push($filterArr['Condition'], "Used");
        }
        if(isset($_GET['unsp'])){
            array_push($filterArr['Condition'], "Unspecified");
        }
    }
    // -------------------- Get searched items --------------------
    $searchedItemsApi = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=RuihuiLu-RuihuiLu-PRD-716e081a6-236635ef&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=20";
    $searchedItemsApi .= "&keywords=$keyword";
    if($categoryID != "-1"){
        $searchedItemsApi .= "&categoryId=$categoryID";
    }
    $number_1 = 0;
    foreach($filterArr as $arr => $val_1){
        $searchedItemsApi .= "&itemFilter($number_1).name=$arr";
        if($arr == "Condition"){
            $number_2 = 0;
            foreach($filterArr['Condition'] as $val_2) {
                $searchedItemsApi .= "&itemFilter($number_1).value($number_2)=$val_2";
                $number_2 ++;
            }
        }
        else{
            $searchedItemsApi .= "&itemFilter($number_1).value=$val_1";
        }
        $number_1 ++;
        
    }
    if($postalCode != ""){
        $searchedItemsApi .= "&buyerPostalCode=$postalCode";
    }
    // -------------------- Get searched items json --------------------
    $searchedItems = file_get_contents($searchedItemsApi);

    if(isset($_GET['itemID'])){
        $chosen = "details";
        $itemID = $_GET['itemID'];
        $detailsApi = "http://open.api.ebay.com/shopping?callName=GetSingleItem&responseencoding=JSON&appid=RuihuiLu-RuihuiLu-PRD-716e081a6-236635ef&siteid=0&version=967&ItemID=$itemID&IncludeSelector=Description,Details,ItemSpecifics";
        $similarsApi = "http://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0&CONSUMER-ID=RuihuiLu-RuihuiLu-PRD-716e081a6-236635ef&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&itemId=$itemID&maxResults=8";
        $details = file_get_contents($detailsApi);
        $similars = file_get_contents($similarsApi);
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HomeWork #6</title>
    <!-------------------- CSS -------------------->
    <style type="text/css">
        *{
            margin: 0px;
            padding: 0px;
        }
        a, a:visited{
            color:black;
            text-decoration: none;
        }
        a:hover{
            color: #afafaf;
        }
        table, td, th{
            margin: 0 auto;
            max-height: 100px;
            border-width: 2px;
            border-style: solid;
            border-color: #bebebe;
            border-collapse: collapse;
        }
        #product_search{
            width: 700px;
            height: 320px;
            margin: 30px auto 20px;
            border-width: 3px;
            border-style: solid;
            border-color: #bebebe;
            background-color: #fafafa;
            text-align: left;
        }
        #head{
            margin-top: 10px;
            margin-bottom: 10px;
        }
        #title{
            font-size: 45px;
            font-style: italic;
            font-weight: lighter;
            text-align: center;
        }
        #separate_line{
            width: 660px;
            margin: 0 auto;
            border: 0;
            border-bottom: 2px solid #bebebe;
        }
        #no_chosen_line{
            color: #8f8f8f;
        }
        #search_options{
            margin-left: 35px;
            font-size: 17px;
        }
        #enable_text{
            margin-left: 10px;
        }
        #distance_options{
            line-height: 20px;
        }
        #distance{
            margin-left: 35px;
        }
        #button{
            margin-left: 230px;
        }
        #submit, #reset{
            font-size: 13px;
        }
        #items_table_header{
            font-weight: bold;
            text-align: center;
        }
        #details_title{
            font-weight: bold;
            font-size: 30px;
        }
        #arrow_1, #arrow_2{
            height: 18px;
            width: 36px;
        }
        #all_similars{
            width: 800px;
            overflow: scroll;
            margin: 0 auto;
            display: none;
            border-width: 2px;
            border-style: solid;
            border-color: #e1e1e1;
        }
        #seller_message, #similar_items{
            text-align: center;
        }
        #seller_message_content{
            display: none;
        }
        .sub_search_parts{
            margin-top: 15px;
        }
        .sub_titles{
            font-weight: bold;
        }
        .condition{
            margin-left: 25px;
        }
        .shipping_options{
            margin-left: 45px;
        }
        .options{
            margin-left: 3px;
        }
        .distance_hints{
            opacity: 0.25;
        }
        .search_distance{
            float:left;
        }
        .items_photo{
            text-align: center;
        }
        .name{
            text-align: left;
        }
        .text{
            color: #acacac;
            margin: 10px auto;
        }
        .error_message_1{
            width: 800px;
            text-align: center;
            margin: 0 auto;
            border-width: 2px;
            border-style: solid;
            border-color: #bebebe;
            background-color: #e2e2e2;
        }
        .seller_message_correct{
            width: 1050px;
            margin: 0 auto;
            border: 0;
        }
        .error_message_2{
            width: 800px;
            margin: 10px 15px;
            border-width: 2px;
            border-style: solid;
            border-color: #e1e1e1;
            background-color: #fff;
            text-align: center;
        }
        .single_similar{
            padding: 30px;
            width: 260px;
            padding-bottom: 10px;
        }
        .imgs{
            max-height: 280px;
        }
    </style>
    <script language="JavaScript">
        function getZipcode(){
            var xmlhttp;
            // ----------------------- Checks browser ------------------------
            if (window.XMLHttpRequest) {
                xmlhttp = new XMLHttpRequest();
            }
            else if (window.ActiveXObject) {
                try {
                    xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
                }
                catch (e) {}
                try {
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                catch(e) {}
                if (!xmlhttp) {
                    window.alert("Cannot Get the Zip!");
                }
            }
            xmlhttp.open("GET", "http://ip-api.com/json", false);
            xmlhttp.send();
            // ----------------------- Gets Info ------------------------
            var locationObj = JSON.parse(xmlhttp.responseText);
            var zip = locationObj.zip;
            // ----------------------- Enables the search ------------------------
            document.getElementById("submit").removeAttribute("disabled");
            document.getElementById("nowzip").value = zip;
            return;
        }

        function enableSearch(){
            var enableBut = document.getElementById("enable");
            var contents = document.getElementsByClassName("distance_hints");
            var buttons = document.getElementsByClassName("distance_inputs");
            var len_1 = contents.length;
            var len_2 = buttons.length;
            // ----------------------- Enables or Disables distance and zip code ------------------------
            if(enableBut.checked){
                for(var x = 0 ; x < len_2 ; x++){
                    buttons[x].removeAttribute("disabled");
                }
                for(var x = 0 ; x < len_1 ; x++){
                    contents[x].style.opacity = 1;
                }
            }
            else{
                for(var x = 0 ; x < len_2 ; x++){
                    buttons[x].setAttribute("disabled", "disabled");
                }
                for(var x = 0 ; x < len_1 ; x++){
                    contents[x].style.opacity = 0.25;
                }
            }
        }

        function changeZip(){
            var defaultZip = document.getElementById("default_zip");
            var inputZip = document.getElementById("input_zip");
            var zipcode = document.getElementById("zipcode");
            // ----------------------- Chooses how to get zip code ------------------------
            if(inputZip.checked){
                zipcode.removeAttribute("disabled");
            }
            else if (defaultZip.checked){
                zipcode.setAttribute("disabled", "disabled");
                zipcode.value = "";
            }
        }

    </script>
</head>
<body onload="getZipcode()">
    <div id="product_search">
        <div id="head">
        <h2 id="title">Product Search</h2>
        <hr id="separate_line" />
        </div>
        <div id="search_options">
            <form name="search_form" id="search_form" method="get" action="index.php">
                <div class="sub_search_parts"><span class="sub_titles">Keyword</span>
                    <input type="text" id="key" name="key" size="18" required />
                </div>
                <div class="sub_search_parts"><span class="sub_titles">Category</span>
                    <select id="category" name="category">
                        <option value="-1">All Categories</option>
                        <option id="no_chosen_line" disabled="">----------------------------------------</option>
                        <option value='550'>Art</option>
                        <option value="2984">Baby</option>
                        <option value="267">Books</option>
                        <option value="11450">Clothing</option>
                        <option value="58058">Shoes & Accessories</option>
                        <option value="26395">Computers/Tablets & Networking</option>
                        <option value="11233"> Health & Beauty</option>
                        <option value="1249">Music and Video Games & Consoles</option>
                    </select>
                </div>

                <div class="sub_search_parts"><span class="sub_titles">Condition</span>
                    <input type="checkbox" class="condition" name="new" id="new" value="chosen" /><span class="options">New</span>
                    <input type="checkbox" class="condition" name="used" id="used" value="chosen" /><span class="options">Used</span>
                    <input type="checkbox" class="condition" name="unsp" id="unsp" value="chosen" /><span class="options">Unspecified</span>
                </div>

                <div class="sub_search_parts"><span class="sub_titles">Shipping Options</span>
                    <input type="checkbox" class="shipping_options" name="localpickup" id="localpickup" value="chosen" /><span class="options">Local Pickup</span>
                    <input type="checkbox" class="shipping_options" name="freeshipping" id="freeshipping" value="chosen" /><span class="options">Free Shipping</span>
                </div>

                <div class="sub_search_parts">
                    <div class="search_distance">
                        <input type="checkbox" id="enable" name="enable" value="chosen" onclick="enableSearch()" /><span class="sub_titles" id="enable_text">Enable Nearby Search</span>
                        <input type="text" class="distance_inputs" id="distance" name="distance" placeholder="10" size="7" disabled />
                        &nbsp;<span class="distance_hints sub_titles">miles from</span>&nbsp;
                    </div>
                    <div class="search_distance" id="distance_options">
                        <input type="radio" id="default_zip" class="distance_inputs" name="location" value="here" disabled onclick="changeZip()"/><span class="distance_hints options">Here</span>
                        <input type="hidden" name="nowzip" id="nowzip"/>
                        <br />
                        <input type="radio" id="input_zip" class="distance_inputs" name="location" value="zip" disabled onclick="changeZip()"/>
                        <input type="text" class="options" id="zipcode" name="zipcode" placeholder="zip code" size="20" required disabled/>
                    </div>
                </div>
                <br />
                <br />
                <br />
                <div id="button">
                    <input type="submit" id="submit" name="submit" value="Search" disabled />
                    <input type="reset" id="reset" name="reset" value="Clear" onclick="resetAllInfo()"/>
                </div>
            </form>
        </div>
    </div>
    <div id="items">
    </div>
    <br />
    <br />
</body>
<script language="JavaScript">
    startSearch();
    function resetAllInfo(){
        var links = window.location.href.split('?');
        window.location.href = links[0];
    }
    function fixValue() {
        document.getElementById("key").value = "<?php echo isset($_GET['key'])?$_GET['key']:'';?>";
        document.getElementById("category").value = "<?php echo isset($_GET['category'])?$_GET['category']:'-1';?>";
        document.getElementById("new").checked = "<?php echo isset($_GET['new'])?'checked':'';?>";
        document.getElementById("used").checked = "<?php echo isset($_GET['used'])?'checked':'';?>";
        document.getElementById("unsp").checked = "<?php echo isset($_GET['unsp'])?'checked':'';?>";
        document.getElementById("localpickup").checked = "<?php echo isset($_GET['localpickup'])?'checked':'';?>";
        document.getElementById("freeshipping").checked = "<?php echo isset($_GET['freeshipping'])?'checked':'';?>";
        document.getElementById("enable").checked = "<?php echo isset($_GET['enable'])?'checked':'';?>";
        document.getElementById("default_zip").checked = "<?php echo (isset($_GET['location'])&&($_GET['location'] == 'here'))?'checked':'';?>";
        document.getElementById("input_zip").checked = "<?php echo (isset($_GET['location'])&&($_GET['location'] == 'zip'))?'checked':'';?>";
        document.getElementById("distance").value = "<?php echo isset($_GET['distance'])?$_GET['distance']:'';?>";
        document.getElementById("zipcode").value = "<?php echo isset($_GET['zipcode'])?$_GET['zipcode']:'';?>";
    }
    function initButtons(){
        var defaultZip = document.getElementById("default_zip");
        var inputZip = document.getElementById("input_zip");
        if(!(defaultZip.checked || inputZip.checked)){
            defaultZip.checked = "checked";
        }
    }
    function sendId(itemId){
        window.location.href =  (window.location.href + "&itemID=" + itemId);
    }
    function getHiddenContent(seller_message, similars){
        // --------------------- Creates seller message (iframe or error) ---------------------
        if(seller_message != undefined && seller_message != ""){
            var seller_frame = document.createElement("iframe");
            seller_frame.setAttribute("class", "seller_message_correct");
            seller_frame.setAttribute("srcdoc", seller_message);
            seller_frame.setAttribute("id", "seller_message_content");
            seller_frame.setAttribute("frameborder", 0);
            document.getElementById("seller_message").appendChild(seller_frame);
            window.setInterval("adjustSellerMes()", 150);
        }
        else{
            var error_seller = document.createElement("div");
            var error_seller_content = document.createTextNode("No Seller Message found.");
            error_seller.setAttribute("id", "seller_message_content");
            error_seller.appendChild(error_seller_content);
            error_seller.setAttribute("class", "error_message_1");
            document.getElementById("seller_message").appendChild(error_seller);
        }
        // --------------------- Creates similar items (div or error) ---------------------
        if(similars != undefined && similars != ""){
            for(var x = 0 ; x < similars.length ; x ++){
                var similars_div = document.createElement("div");
                var similarItemID = similars[x].itemId;
                var similarItemTitle = similars[x].title;
                var similarItemPicture = similars[x].imageURL;
                var similarItemPrice = "$" + similars[x].buyItNowPrice.__value__;
                similars_div.setAttribute("class", "single_similar");
                var content = "<img src='" + similarItemPicture + "'/><br /><p><a href='#' onclick='sendId(" + similarItemID + ")'>" + similarItemTitle + "</a></p><p><strong>" + similarItemPrice + "</strong></p>";
                similars_div.innerHTML = content;
                document.getElementById("all_similars").appendChild(similars_div);
            }  
        }
        else{
            var similars_error_div = document.createElement("div");
            var similars_error_text = document.createTextNode("No Similar Item found.");
            similars_error_div.setAttribute("class", "error_message_2");
            similars_error_div.appendChild(similars_error_text);
            document.getElementById("all_similars").appendChild(similars_error_div);
        }
    }
    function getItems(json_file){
        var itemsJsonInfo = json_file; 
        var table = "";
        var itemSearched = itemsJsonInfo.findItemsAdvancedResponse[0].searchResult[0].item;
        if(itemSearched != undefined){
            table += "<table><tr id='items_table_header'><td>Index</td><td>Photo</td><td>Name</td><td>Price</td><td>Zip Code</td><td>Condition</td><td>Shipping Option</td></tr>";
            for(var x = 0 ; x < itemSearched.length ; x++){
                var itemId = itemSearched[x].itemId;
                var itemPhoto = itemSearched[x].galleryURL[0];
                var itemName = itemSearched[x].title[0];
                var itemPrice = "$" + itemSearched[x].sellingStatus[0].currentPrice[0].__value__;
                var itemZip = (itemSearched[x].postalCode == undefined)?"N/A":itemSearched[x].postalCode;
                var itemCondition = (itemSearched[x].condition == undefined)?"N/A":itemSearched[x].condition[0].conditionDisplayName[0];
                var itemShip = "";
                if(itemSearched[x].shippingInfo == undefined || itemSearched[x].shippingInfo[0].shippingServiceCost == undefined){
                    itemShip = "N/A";
                }
                else{
                    if(itemSearched[x].shippingInfo[0].shippingServiceCost[0].__value__ != "0.0"){itemShip = "$" + itemSearched[x].shippingInfo[0].shippingServiceCost[0].__value__;}
                    else{itemShip = "Free Shipping";}
                }
                table += "<tr><td>" + (x + 1) + "</td><td class='items_photo'><img src='" + itemPhoto + "'/></td><td><a href='#' onclick='sendId(" + itemId + ")'>" + itemName + "</a></td><td>" + itemPrice + "</td><td>" + itemZip + "</td><td>" + itemCondition + "</td><td>" + itemShip + "</td></tr>";
            }
            table += "</table>";
        }
        else {
            table += "<div class='error_message_1'>No Records has been found</div>";
        }
        changeContent(table);
    }
    function changeContent(content){
        document.getElementById("items").innerHTML = content;
    }
    function getDetails(json_file_1, json_file_2){
        var table = "";
        var detailsInfo = json_file_1;
        var similarsInfo = json_file_2;
        var picture = detailsInfo.Item.PictureURL;
        var title = detailsInfo.Item.Title;
        var subtitle = detailsInfo.Item.Subtitle;
        var currentPrice = detailsInfo.Item.CurrentPrice;
        var location = detailsInfo.Item.Location;
        var postalCode = detailsInfo.Item.PostalCode;
        var userID = detailsInfo.Item.Seller.UserID;
        var returns = detailsInfo.Item.ReturnsAccepted;
        var specifics = detailsInfo.Item.ItemSpecifics;
        var description = detailsInfo.Item.Description;
        var similars = similarsInfo.getSimilarItemsResponse.itemRecommendations.item;

        table += "<table><caption id='details_title'>Item Details</caption>";
        if(picture !== undefined){table += "<tr><th class='name'>Photo</th><td><img class='imgs' src='" + picture + "'></td></tr>";}
        if(title !== undefined){table += "<tr><th class='name'>Title</th><td>" + title + "</td></tr>";}
        if(subtitle !== undefined){table += "<tr><th class='name'>Subtitle</th><td>" + subtitle + "</td></tr>";}
        if(currentPrice !== undefined){table += "<tr><th class='name'>Price</th><td>" + currentPrice.Value + currentPrice.CurrencyID + "</td></tr>";}
        if(location !== undefined){table += "<tr><th class='name'>Location</th><td>" + location + ", " + postalCode + "</td></tr>";}
        if(userID !== undefined){table += "<tr><th class='name'>Seller</th><td>" + userID + "</td></tr>";}
        if(returns !== undefined){table += "<tr><th class='name'>Return Policy(US)</th><td>" + returns + "</td></tr>";}
        if(specifics !== undefined){
            var properties = specifics.NameValueList;
            for(var x = 0 ; x < properties.length; x ++){table += "<tr><th class='name'>" + properties[x].Name + "</th><td>" + properties[x].Value[0] + "</td></tr>";}
        }
        table += "</table>";
        table += "<div>";
        table += "<div id='seller_message'><div id='show_hide_1' class='text'>click to show seller message</div>";
        table += "<img id='arrow_1' src='http://csci571.com/hw/hw6/images/arrow_down.png' onclick='sellerShowAndHidden()' /><br /></div>";
        table += "<div id='similar_items'><div id='show_hide_2' class='text'>click to show similar items</div>";
        table += "<img id='arrow_2' src='http://csci571.com/hw/hw6/images/arrow_down.png' onclick='smShowAndHidden()' /><br /></div>"
        table += "</div>";
        changeContent(table);
        var all_similar_items = document.createElement("div");
        all_similar_items.setAttribute("id","all_similars");
        document.getElementById("similar_items").appendChild(all_similar_items);
        getHiddenContent(description, similars);
    }
    function adjustSellerMes(){
        var seller_frame = document.getElementById("seller_message_content");
        try{
            var bodyHeight = seller_frame.contentWindow.document.body.scrollHeight;
            var docuHeight = seller_frame.contentWindow.document.documentElement.scrollHeight;
            var finalHeight = Math.max(bodyHeight, docuHeight);
            seller_frame.height = finalHeight;
        }catch(e){
        }
    }
    function sellerShowAndHidden(){
        var arrow = document.getElementById("arrow_1");
        var text = document.getElementById("show_hide_1");
        var content = document.getElementById("seller_message_content");
        if(content.style.display == "block"){
            text.innerHTML = "click to show seller message";
            arrow.setAttribute("src", "http://csci571.com/hw/hw6/images/arrow_down.png");
            content.style.display = "none";
        }
        else{
            var arrow_o = document.getElementById("arrow_2");
            var content_o = document.getElementById("all_similars");
            var text_o = document.getElementById("show_hide_2");
            if(content_o.style.display == "flex"){
                text_o.innerHTML = "click to show similar items";
                arrow_o.setAttribute("src", "http://csci571.com/hw/hw6/images/arrow_down.png");
                content_o.style.display = "none";
            }
            text.innerHTML = "click to hide seller message";
            arrow.setAttribute("src", "http://csci571.com/hw/hw6/images/arrow_up.png");
            content.style.display = "block";
        }
    }
    function smShowAndHidden(){
        var arrow = document.getElementById("arrow_2");
        var content = document.getElementById("all_similars");
        var text = document.getElementById("show_hide_2");
        if(content.style.display == "flex"){
            text.innerHTML = "click to show similar items";
            arrow.setAttribute("src", "http://csci571.com/hw/hw6/images/arrow_down.png");
            content.style.display = "none";
        }
        else{
            var arrow_o = document.getElementById("arrow_1");
            var text_o = document.getElementById("show_hide_1");
            var content_o = document.getElementById("seller_message_content");
            if(content_o.style.display == "block"){
                text_o.innerHTML = "click to show seller message";
                arrow_o.setAttribute("src", "http://csci571.com/hw/hw6/images/arrow_down.png");
                content_o.style.display = "none";
            }
            content.style.display = "flex";
            text.innerHTML = "click to hide similar items";
            arrow.setAttribute("src", "http://csci571.com/hw/hw6/images/arrow_up.png");
        }
    }
    function startSearch(){
        fixValue();
        initButtons();
        enableSearch();
        changeZip();
        try{
            <?php if($error == "true"): ?>
                changeContent("<div class='error_message_1'>Zipcode is invalid</div>");
            <?php else: ?>
                <?php if(!isset($_GET['itemID'])): ?>
                getItems(<?php echo $searchedItems; ?>);
                <?php else: ?>
                getDetails(<?php echo $details; ?>, <?php echo $similars; ?>);
                <?php endif; ?>
            <?php endif; ?>
        }
        catch(e){
        }
    }
</script>
</html>