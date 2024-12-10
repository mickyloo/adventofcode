use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use std::collections::{HashMap, HashSet};
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "08";
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............"; // TODO: Add the test input

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn parse_satellites(grid: Vec<Vec<char>>) -> HashMap<char, Vec<(i32, i32)>> {
        let size = grid.len();
        let mut satellites: HashMap<char, Vec<(i32, i32)>> = HashMap::new();
        for i in 0..size {
            for j in 0..size {
                let c = grid[i][j];
                if c == '.' {
                    continue;
                }
                satellites
                    .entry(c)
                    .or_insert_with(Vec::new)
                    .push((i as i32, j as i32));
            }
        }
        satellites
    }

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        let grid = read_to_2d(reader);
        let satellites = parse_satellites(grid.clone());

        let mut antinodes: HashSet<(i32, i32)> = HashSet::new();
        let size = grid.len() as i32;
        for group in satellites.values() {
            for pair in group.iter().combinations(2) {
                let nodes = find_antinodes(pair, size, 1);
                antinodes.extend(nodes);
            }
        }

        let answer = antinodes.len();
        Ok(answer)
    }

    fn find_antinodes(pair: Vec<&(i32, i32)>, size: i32, limit: usize) -> Vec<(i32, i32)> {
        let mut nodes: Vec<(i32, i32)> = Vec::new();

        let delta_y = pair[1].1 - pair[0].1;
        let delta_x = pair[1].0 - pair[0].0;

        for i in 1..limit + 1 {
            let anti_1 = (
                pair[0].0 - delta_x * i as i32,
                pair[0].1 - delta_y * i as i32,
            );
            if anti_1.0 >= 0 && anti_1.0 < size && anti_1.1 >= 0 && anti_1.1 < size {
                nodes.push(anti_1);
            }

            let anti_2 = (
                pair[1].0 + delta_x * i as i32,
                pair[1].1 + delta_y * i as i32,
            );
            if anti_2.0 >= 0 && anti_2.0 < size && anti_2.1 >= 0 && anti_2.1 < size {
                nodes.push(anti_2);
            }
        }
        nodes
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(14, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R) -> Result<usize> {
        let grid = read_to_2d(reader);
        let satellites = parse_satellites(grid.clone());

        let mut antinodes: HashSet<(i32, i32)> = HashSet::new();
        let size = grid.len() as i32;
        for group in satellites.values() {
            for pair in group.iter().combinations(2) {
                let nodes = find_antinodes(pair.clone(), size, size as usize);
                antinodes.extend(nodes);
                antinodes.insert((pair[0].0, pair[0].1));
                antinodes.insert((pair[1].0, pair[1].1));
            }
        }

        let answer = antinodes.len();
        Ok(answer)
    }

    assert_eq!(34, part2(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
