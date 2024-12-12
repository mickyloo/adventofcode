use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use std::collections::HashSet;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "10"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");
const CHARS: [char; 10] = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];

const TEST: &str = "\
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732";

fn main() -> Result<()> {
    start_day(DAY);

    fn find_paths(grid: &Vec<Vec<char>>) -> Vec<Vec<(i32, i32)>> {
        let mut work: Vec<Vec<(i32, i32)>> = vec![];
        let size = grid.len();
        for i in 0..size {
            for j in 0..size {
                if grid[i][j] == '0' {
                    work.push(vec![(i as i32, j as i32)]);
                }
            }
        }

        let mut paths = vec![];
        while !work.is_empty() {
            let path = work.pop().unwrap();
            let curr = path.last().unwrap();
            if grid[curr.0 as usize][curr.1 as usize] == '9' {
                paths.push(path);
                continue;
            }

            neighbors(&grid, *curr).iter().for_each(|x| {
                let mut new = path.clone();
                new.push(*x);
                work.push(new);
            });
        }
        paths
    }
    fn neighbors(grid: &Vec<Vec<char>>, curr: (i32, i32)) -> Vec<(i32, i32)> {
        let elevation = grid[curr.0 as usize][curr.1 as usize].to_digit(10).unwrap();
        let size = grid.len();
        let next_elevation: char = CHARS[(elevation + 1) as usize];
        let mut neighbors: Vec<(i32, i32)> = vec![];

        //up
        if curr.0 > 0 && grid[(curr.0 - 1) as usize][curr.1 as usize] == next_elevation {
            neighbors.push((curr.0 - 1, curr.1));
        }
        //down
        if curr.0 < (size - 1) as i32
            && grid[(curr.0 + 1) as usize][curr.1 as usize] == next_elevation
        {
            neighbors.push((curr.0 + 1, curr.1));
        }
        //left
        if curr.1 > 0 && grid[curr.0 as usize][(curr.1 - 1) as usize] == next_elevation {
            neighbors.push((curr.0, curr.1 - 1));
        }
        //right
        if curr.1 < (size - 1) as i32
            && grid[curr.0 as usize][(curr.1 + 1) as usize] == next_elevation
        {
            neighbors.push((curr.0, curr.1 + 1));
        }

        neighbors
    }

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        let grid = read_to_2d(reader);
        let paths = find_paths(&grid);

        let trails: HashSet<_> = paths
            .iter()
            .map(|path| (path.first().unwrap(), path.last().unwrap()))
            .collect();

        let answer = trails.len();
        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(36, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R) -> Result<usize> {
        let grid = read_to_2d(reader);
        let paths = find_paths(&grid);
        let answer = paths.len();
        Ok(answer)
    }

    assert_eq!(81, part2(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
