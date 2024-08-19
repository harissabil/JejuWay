

import time
import random
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.chrome import ChromeDriverManager
from tqdm import tqdm  # Import tqdm for progress bar
import pandas as pd  # Import pandas to create DataFrame

chrome_options = webdriver.ChromeOptions()
chrome_options.add_argument("--disable-gpu")
chrome_options.add_argument("--no-sandbox")
chrome_options.add_argument("window-size=1920,1080")

# List of user-agents
user_agents = [
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Safari/605.1.15",
    # Add more user-agent strings
]
chrome_options.add_argument(f"user-agent={random.choice(user_agents)}")

# Initialize the WebDriver without explicitly passing the path
driver = webdriver.Chrome(service=webdriver.chrome.service.Service(ChromeDriverManager().install()), options=chrome_options)

# List of queries to obtain links and titles
queries = [
    General Itineraries
    "best Jeju itinerary for 3 days",
    "Jeju Island travel guide",
    "perfect 5-day Jeju itinerary",
    "Jeju itinerary for solo travelers",
    "Jeju Island itinerary for couples",

    # Family and Group Travel
    "Jeju family-friendly itinerary",
    "Jeju itinerary for large groups",
    "kid-friendly Jeju Island itinerary",
    "Jeju Island itinerary for seniors",
    "Jeju Island travel plan for families",

    # Activity-Based Itineraries
    "Jeju hiking itinerary",
    "adventure activities in Jeju itinerary",
    "Jeju Island beach itinerary",
    "Jeju cultural and historical itinerary",
    "Jeju Island food and restaurant itinerary",

    Seasonal Itineraries
    "Jeju winter itinerary",
    "Jeju spring cherry blossom itinerary",
    "Jeju summer vacation itinerary",
    "Jeju autumn foliage itinerary",
    "seasonal Jeju travel guide",

    Special Interest
    "Jeju honeymoon itinerary",
    "budget Jeju itinerary",
    "luxury Jeju Island itinerary",
    "photography spots in Jeju itinerary",
    "Jeju off-the-beaten-path itinerary",

    # Duration-Based Itineraries
    "Jeju 1-day itinerary",
    "week-long Jeju itinerary",
    "Jeju Island weekend trip itinerary",
    "Jeju short trip itinerary",
    "Jeju long-stay itinerary"
]
results = []  # List to store tuples of (query, title, link)

# Specify the number of pages on Google search, each page contains 10 links
n_pages = 10

# Iterate over each query
for query in queries:
    print(f"Scraping results for query: {query}")
    
    # Wrap the loop with tqdm for a progress bar
    for page in tqdm(range(1, n_pages + 1), desc=f"Scraping {query} Pages"):
        url = "http://www.google.com/search?q=" + query + "&start=" + str((page - 1) * 10)
        driver.get(url)
        
        # Introduce a random delay between requests to mimic human behavior
        time.sleep(random.uniform(2, 5))
        
        # Increase the wait time to 20 seconds
        try:
            WebDriverWait(driver, 20).until(
                EC.presence_of_element_located((By.CLASS_NAME, "yuRUbf"))
            )
        except Exception as e:
            print(f"Error waiting for page to load: {e}")
            continue  # Continue to the next page if there's an error

        soup = BeautifulSoup(driver.page_source, 'html.parser')
        
        search = soup.find_all('div', class_="yuRUbf")
        for h in search:
            link = h.a.get('href')
            title_tag = h.find('h3')  # Target the 'h3' tag without relying on specific class names
            if title_tag:
                title = title_tag.get_text().strip()  # Extract and clean the title text
                results.append((query, title, link))  # Store query, cleaned title, and link

# Convert the results into a DataFrame with three columns: 'Query', 'Title', and 'Link'
df = pd.DataFrame(results, columns=['Query', 'Title', 'Link'])

# Display the DataFrame
print(df)

# Save the DataFrame to a CSV file if needed
df.to_csv('jeju_itinerary_results_6.csv', index=False)

# Close the WebDriver
driver.quit()

# # gabungkan jeju itinerary results 1-6
# df1 = pd.read_csv('jeju_itinerary_results_1.csv')
# df2 = pd.read_csv('jeju_itinerary_results_2.csv')
# df3 = pd.read_csv('jeju_itinerary_results_3.csv')
# df4 = pd.read_csv('jeju_itinerary_results_4.csv')
# df5 = pd.read_csv('jeju_itinerary_results_5.csv')
# df6 = pd.read_csv('jeju_itinerary_results_6.csv')

# df = pd.concat([df1, df2, df3, df4, df5, df6], ignore_index=True)

# # drop duplicate rows
# df = df.drop_duplicates(subset='Link')

# # save to csv
# df.to_csv('jeju_itinerary_results.csv', index=False)