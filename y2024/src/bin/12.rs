use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use std::collections::{HashMap, HashSet};
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "12"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        let grid = read_to_2d(reader);
        let mut visited: HashMap<(usize, usize), usize> = HashMap::new();
        let mut islands: Vec<Vec<(usize, usize)>> = vec![];

        for i in 0..grid.len() {
            for j in 0..grid.len() {
                if visited.contains_key(&(i, j)) {
                    continue;
                }
                let mut island = vec![];
                dfs(&mut visited, &mut island, &grid, (i, j));
                islands.push(island);
            }
        }

        let answer = islands
            .iter()
            .map(|island| {
                let area = island.len();
                let perimeter = island.into_iter().map(|x| visited[&x]).sum::<usize>();
                area * perimeter
            })
            .sum::<usize>();

        Ok(answer)
    }
    fn dfs(
        visited: &mut HashMap<(usize, usize), usize>,
        island: &mut Vec<(usize, usize)>,
        grid: &Vec<Vec<char>>,
        curr: (usize, usize),
    ) {
        if visited.contains_key(&curr) {
            return;
        }
        island.push(curr);

        let ch = grid[curr.0][curr.1];
        let size = grid.len();
        let mut borders = 4;
        let mut neighbors: Vec<(usize, usize)> = vec![];

        //up
        if curr.0 > 0 && grid[curr.0 - 1][curr.1] == ch {
            borders -= 1;
            neighbors.push((curr.0 - 1, curr.1));
        }
        //down
        if curr.0 < (size - 1) && grid[curr.0 + 1][curr.1] == ch {
            borders -= 1;
            neighbors.push((curr.0 + 1, curr.1));
        }
        //left
        if curr.1 > 0 && grid[curr.0][curr.1 - 1] == ch {
            borders -= 1;
            neighbors.push((curr.0, curr.1 - 1));
        }
        //right
        if curr.1 < (size - 1) && grid[curr.0][curr.1 + 1] == ch {
            borders -= 1;
            neighbors.push((curr.0, curr.1 + 1));
        }

        visited.insert(curr, borders);
        for next in neighbors {
            dfs(visited, island, grid, next);
        }
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(1930, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    // println!("\n=== Part 2 ===");
    //
    // fn part2<R: BufRead>(reader: R) -> Result<usize> {
    //     Ok(0)
    // }
    //
    // assert_eq!(1206, part2(BufReader::new(TEST.as_bytes()))?);
    //
    // let input_file = BufReader::new(File::open(INPUT_FILE)?);
    // let result = time_snippet!(part2(input_file)?);
    // println!("Result = {}", result);
    //endregion

    Ok(())
}
